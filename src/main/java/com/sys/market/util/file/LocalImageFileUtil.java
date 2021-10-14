package com.sys.market.util.file;

import com.sys.market.util.ImageUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Profile("test")
public class LocalImageFileUtil implements ImageFileUtil {
    @Value("${resources.location}")
    private String resourceLocation;
    @Value("${resources.path}")
    private String resourcePath;

    private static final int IMAGE_MAX_WIDTH = 1280;
    private static final int THUMB_IMAGE_MAX_WIDTH = 300;

    // 이미지를 업로드하고 경로를 반환
    @Override
    public String uploadImage(MultipartFile file) {
        if (file.getSize() <= 0) return null;

        try {
            Date createDate = new Date();
            String extension = ImageUtil.getExtension(file);

            // 이미지 저장
            String path = generateFilePath(extension, createDate);
            File target = new File(resourceLocation, path);
            if (!target.getParentFile().exists())
                target.getParentFile().mkdirs();
            if (!target.exists())
                target.createNewFile();

            save(ImageUtil.resize(file, IMAGE_MAX_WIDTH), target, extension);

            // 썸네일 저장
            String thumbPath = nameToThumbName(path);
            File thumbTarget = new File(resourceLocation, thumbPath);
            if (!target.exists())
                thumbTarget.createNewFile();

            save(ImageUtil.resizeAndCropToCenter(file, THUMB_IMAGE_MAX_WIDTH), thumbTarget, extension);

            return pathToImageUrl(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String uploadProfileImage(MultipartFile file, String userId) {
        if (file.getSize() <= 0) return null;

        try {
            String extension = ImageUtil.getExtension(file);
            String path = "profile/" + userId + "." + extension;
            File target = new File(resourceLocation, path);

            if (!target.getParentFile().exists()) {
                target.getParentFile().mkdirs();
            }
            if (!target.exists()) {
                target.createNewFile();
            }

            save(ImageUtil.resizeAndCropToCenter(file, THUMB_IMAGE_MAX_WIDTH), target, extension);

            return pathToImageUrl(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteImage(String path) {
        File target = new File(resourceLocation, path);
        if (target.exists()) {
            try {
                Files.delete(target.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String nameToThumbName(String name) {
        if (name == null) return null;

        int index = name.lastIndexOf(".");
        return name.substring(0, index) + "_s" + name.substring(index);
    }

    private String generateFilePath(String extension, Date createDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        String folderName = dateFormat.format(createDate);
        String fileName = UUID.randomUUID().toString() + "." + extension;

        return folderName + "/" + fileName;
    }

    private String pathToImageUrl(String path) {
        if (path == null) return null;
        return "http://localhost:8080" + resourcePath + path;
    }

    private void save(BufferedImage image, File file, String extension) throws IOException {

        // jpg일 경우 퀄리티 설정
        try {
            FileImageOutputStream bos = new FileImageOutputStream(file);
            if (extension.equals("jpg")) {
                ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
                ImageWriteParam iwp = writer.getDefaultWriteParam();
                iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                iwp.setCompressionQuality(1f);

                writer.setOutput(bos);
                writer.write(null, new IIOImage(image, null, null), iwp);
                writer.dispose();
            } else {
                ImageIO.write(image, extension, bos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
