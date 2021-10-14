package com.sys.market.util;

import com.sys.market.advice.exception.CInvalidImageFormatException;
import org.apache.tika.Tika;
import org.imgscalr.Scalr;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

public class ImageUtil {
    private ImageUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static final HashMap<String, String> IMAGE_FILE_EXTENSION = new HashMap<>();
    static {
        IMAGE_FILE_EXTENSION.put("image/png", "png");
        IMAGE_FILE_EXTENSION.put("image/jpeg", "jpg");
        IMAGE_FILE_EXTENSION.put("image/gif", "gif");
    }

    // 이미지 사이즈 조절
    public static BufferedImage resize(MultipartFile file, int maxWidth) throws IOException {
        BufferedImage originalImage = ImageIO.read(file.getInputStream());

        if(originalImage.getWidth() > maxWidth){
            return Scalr.resize(originalImage, Scalr.Mode.FIT_TO_WIDTH, maxWidth);
        }else{
            return originalImage;
        }
    }

    // 이미지 사이즈 조절 후 정사각형으로 크롭
    public static BufferedImage resizeAndCropToCenter(MultipartFile file, int width) throws IOException {
        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        int minPx = Math.min(originalImage.getWidth(), originalImage.getHeight());

        BufferedImage croppedImage = Scalr.crop(originalImage, (originalImage.getWidth() - minPx)/2, (originalImage.getHeight() - minPx)/2, minPx, minPx);

        return Scalr.resize(croppedImage, width);
    }

    public static String getExtension(MultipartFile file) throws IOException{
        Tika tika = new Tika();
        String mimeType = tika.detect(file.getBytes());
        String extension = IMAGE_FILE_EXTENSION.get(mimeType);
        if(null == extension){
            throw new CInvalidImageFormatException();
        }
        return extension;
    }
}
