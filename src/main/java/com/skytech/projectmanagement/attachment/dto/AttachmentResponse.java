package com.skytech.projectmanagement.attachment.dto;

import java.time.Instant;
import com.skytech.projectmanagement.attachment.entity.Attachment;

public record AttachmentResponse(

        Integer id,

        String fileName,

        String downloadUrl,

        Integer uploadedByUserId,

        Instant createdAt) {

    public static AttachmentResponse fromEntity(Attachment attachment, String downloadUrl) {
        return new AttachmentResponse(attachment.getId(), attachment.getFileName(), downloadUrl,
                attachment.getUserId(), attachment.getCreatedAt());
    }
}
