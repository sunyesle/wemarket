package com.sys.market.util.file;

import org.springframework.web.multipart.MultipartFile;

public interface ImageFileUtil {
    String uploadImage(MultipartFile file);
    String uploadProfileImage(MultipartFile file, String userId);
    void deleteImage(String url);
    String nameToThumbName(String name);
}
