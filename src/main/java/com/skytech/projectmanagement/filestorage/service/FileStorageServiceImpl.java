package com.skytech.projectmanagement.filestorage.service;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.skytech.projectmanagement.common.exception.FileStorageException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    private final Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile file, String prefix) {
        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new FileStorageException("File rỗng hoặc không có tên.");
        }

        try {
            Map options = ObjectUtils.asMap("folder", prefix, "resource_type", "auto");

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), options);

            String publicId = (String) uploadResult.get("public_id");
            String resourceType = (String) uploadResult.get("resource_type");
            String format = (String) uploadResult.get("format");

            String objectName = resourceType + "/" + publicId + "." + format;

            log.info("File uploaded successfully to Cloudinary: {}", publicId);
            return objectName;

        } catch (IOException e) {
            log.error("Error uploading file to Cloudinary: {}", e.getMessage());
            throw new FileStorageException("Lỗi khi tải file lên Cloudinary: " + e.getMessage(), e);
        }
    }

    @Override
    public String getFileUrl(String objectName) {
        try {
            String resourceType = getResourceType(objectName);
            String publicIdWithFormat = getPublicIdWithFormat(objectName);

            return cloudinary.url().resourceType(resourceType).generate(publicIdWithFormat);
        } catch (Exception e) {
            log.error("Error generating Cloudinary URL for object: {}", objectName, e);
            return null;
        }
    }

    @Override
    public void deleteFile(String objectName) {
        if (objectName == null || objectName.isBlank()) {
            log.warn("Attempted to delete a file with null or blank object name.");
            return;
        }

        try {
            String decodedObjectName = URLDecoder.decode(objectName, StandardCharsets.UTF_8);

            String resourceType = getResourceType(decodedObjectName);
            String publicId = getPublicId(decodedObjectName);

            Map options = ObjectUtils.asMap("resource_type", resourceType);

            cloudinary.uploader().destroy(publicId, options);
            log.info("Successfully deleted file from Cloudinary: {}", publicId);
        } catch (IOException e) {
            log.error("Could not delete file '{}' from Cloudinary: {}", objectName, e.getMessage());
        } catch (Exception e) {
            log.error("Error parsing objectName for deletion: {}", objectName, e);
        }
    }

    @Override
    public String getPresignedDownloadUrl(String objectName) {
        if (objectName == null || objectName.isBlank()) {
            return null;
        }

        try {
            String resourceType = getResourceType(objectName);
            String publicIdWithFormat = getPublicIdWithFormat(objectName);

            return cloudinary.url().resourceType(resourceType).secure(true).signed(true)
                    .generate(publicIdWithFormat);

        } catch (Exception e) {
            log.error("Lỗi khi tạo pre-signed URL cho '{}': {}", objectName, e.getMessage());
            throw new FileStorageException("Không thể tạo URL tải xuống: " + e.getMessage(), e);
        }
    }



    private String getResourceType(String objectName) {
        int slashIndex = objectName.indexOf('/');
        if (slashIndex == -1) {
            throw new IllegalArgumentException(
                    "Invalid objectName format. Expected 'resourceType/publicId.format'");
        }
        return objectName.substring(0, slashIndex);
    }

    private String getPublicId(String objectName) {
        int slashIndex = objectName.indexOf('/');
        int lastDotIndex = objectName.lastIndexOf('.');
        if (slashIndex == -1 || lastDotIndex == -1 || lastDotIndex < slashIndex) {
            throw new IllegalArgumentException(
                    "Invalid objectName format. Expected 'resourceType/publicId.format'");
        }
        return objectName.substring(slashIndex + 1, lastDotIndex);
    }

    private String getPublicIdWithFormat(String objectName) {
        int slashIndex = objectName.indexOf('/');
        if (slashIndex == -1) {
            throw new IllegalArgumentException(
                    "Invalid objectName format. Expected 'resourceType/publicId.format'");
        }
        return objectName.substring(slashIndex + 1);
    }

}
