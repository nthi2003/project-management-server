package com.skytech.projectmanagement.filestorage.service;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import com.skytech.projectmanagement.common.exception.FileStorageException;
import com.skytech.projectmanagement.filestorage.config.MinioConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.MinioException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;

    @PostConstruct
    private void initializeBucket() {
        try {
            boolean found = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(minioConfig.getBucketName()).build());
            if (!found) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(minioConfig.getBucketName()).build());
                log.info("MinIO bucket '{}' created successfully.", minioConfig.getBucketName());
            } else {
                log.info("MinIO bucket '{}' already exists.", minioConfig.getBucketName());
            }
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            log.error("Could not initialize MinIO bucket: {}", e.getMessage());
        }
    }

    @Override
    public String uploadFile(MultipartFile file, String prefix) {
        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new FileStorageException("File rỗng hoặc không có tên.");
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            int lastDotIndex = originalFilename.lastIndexOf('.');
            if (lastDotIndex > 0) {
                fileExtension = originalFilename.substring(lastDotIndex);
            }
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
            String objectName = prefix + uniqueFileName;

            minioClient.putObject(PutObjectArgs.builder().bucket(minioConfig.getBucketName())
                    .object(objectName).stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType()).build());

            log.info("File uploaded successfully to MinIO: {}", objectName);
            return objectName;

        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            log.error("Error uploading file to MinIO: {}", e.getMessage());
            throw new FileStorageException("Lỗi khi tải file lên MinIO: " + e.getMessage(), e);
        }
    }

    @Override
    public String getFileUrl(String objectName) {
        return minioConfig.getEndpoint() + "/" + minioConfig.getBucketName() + "/" + objectName;
    }

    @Override
    public void deleteFile(String objectName) {
        if (objectName == null || objectName.isBlank()) {
            log.warn("Attempted to delete a file with null or blank object name.");
            return;
        }

        try {
            String decodedObjectName = URLDecoder.decode(objectName, StandardCharsets.UTF_8);

            minioClient.removeObject(RemoveObjectArgs.builder().bucket(minioConfig.getBucketName())
                    .object(decodedObjectName).build());
            log.info("Successfully deleted file from MinIO: {}", decodedObjectName);
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            log.error("Could not delete file '{}' from MinIO: {}", objectName, e.getMessage());
        }
    }

}
