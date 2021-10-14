package com.sys.market.util.file;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.sys.market.util.ImageUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Profile("prod")
public class FirebaseImageFileUtil implements ImageFileUtil{
    @Value("${firebase.bucket-name}")
    private String bucketName;
    @Value("${firebase.service-account-path}")
    private String serviceAccountPath;
    @Value("${firebase.image-url-pattern}")
    private String imageUrlPattern;
    @Value("${firebase.image-url-format}")
    private String imageUrlFormat;

    private static final int IMAGE_MAX_WIDTH = 1280;
    private static final int THUMB_IMAGE_MAX_WIDTH = 300;

    private Pattern pattern;
    private Storage storage;

    @EventListener
    public void init(ApplicationReadyEvent event){
        pattern = Pattern.compile(imageUrlPattern);
        try{
            ClassPathResource serviceAccount = new ClassPathResource(serviceAccountPath);
            Credentials credentials = GoogleCredentials.fromStream(serviceAccount.getInputStream());

            storage = StorageOptions.newBuilder()
                    .setCredentials(credentials)
                    .build().getService();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 이미지를 업로드하고 url를 반환
    @Override
    public String uploadImage(MultipartFile file){
        if(file.getSize() <= 0) return null;

        try {
            Date createDate = Calendar.getInstance().getTime();
            String extension = ImageUtil.getExtension(file);

            String name = generateFilePath(extension, createDate);
            BlobId blobId = BlobId.of(bucketName, name);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(file.getContentType())
                    .build();

            BufferedImage image = ImageUtil.resize(file, IMAGE_MAX_WIDTH);
            storage.create(blobInfo, bufferedImageToByteArray(image, extension));

            // 썸네일 저장
            String thumbName = nameToThumbName(name);
            BlobId thumbBlobId = BlobId.of(bucketName, thumbName);
            BlobInfo thumbBlobInfo = BlobInfo.newBuilder(thumbBlobId)
                    .setContentType(file.getContentType())
                    .build();

            BufferedImage thumbImage = ImageUtil.resizeAndCropToCenter(file, THUMB_IMAGE_MAX_WIDTH);
            storage.create(thumbBlobInfo, bufferedImageToByteArray(thumbImage, extension));

            return String.format(imageUrlFormat, name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String uploadProfileImage(MultipartFile file, String userId){
        if(file.getSize() <= 0) return null;

        try {
            String extension = ImageUtil.getExtension(file);
            String path = "profile/" + userId + "." + extension;

            BlobId blobId = BlobId.of(bucketName, path);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(file.getContentType())
                    .setCacheControl("max-age=0")
                    .build();

            BufferedImage thumbImage = ImageUtil.resizeAndCropToCenter(file, THUMB_IMAGE_MAX_WIDTH);
            storage.create(blobInfo, bufferedImageToByteArray(thumbImage, extension));

            return String.format(imageUrlFormat, path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteImage(String url) {
        Matcher m = pattern.matcher(url);
        if(m.find()){
            String path = m.group(1);
            BlobId blobId = BlobId.of(bucketName, path);

            storage.delete(blobId);
        }
    }

    @Override
    public String nameToThumbName(String name){
        if(name == null) return null;

        int index = name.lastIndexOf(".");
        return name.substring(0, index) + "_s" + name.substring(index);
    }

    private String generateFilePath(String extension, Date createDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        String folderName = dateFormat.format(createDate);
        String fileName = UUID.randomUUID().toString() + "." + extension;

        return folderName + "/" + fileName;
    }

    private byte[] bufferedImageToByteArray(BufferedImage image, String extension) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        // jpg일 경우 퀄리티 설정
        if(extension.equals("jpg")){
            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
            ImageWriteParam iwp = writer.getDefaultWriteParam();
            iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            iwp.setCompressionQuality(1f);

            ImageOutputStream ios = ImageIO.createImageOutputStream(bos);
            writer.setOutput(ios);
            writer.write(null, new IIOImage(image, null, null), iwp);
            writer.dispose();
        }else{
            ImageIO.write(image, extension, bos);
        }
        return bos.toByteArray();
    }
}