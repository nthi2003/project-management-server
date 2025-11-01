package com.skytech.projectmanagement.attachment.service;

import java.util.List;
import com.skytech.projectmanagement.attachment.dto.AttachmentResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {

    List<AttachmentResponse> getAttachmentsForEntity(String entityType, Integer entityId);

    AttachmentResponse createAttachment(MultipartFile file, String entityType, Integer entityId,
            Integer userId);
}
