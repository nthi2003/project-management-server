package com.skytech.projectmanagement.filestorage.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String uploadFile(MultipartFile file, String prefix);

    String getFileUrl(String objectName);

    void deleteFile(String objectName);
}
