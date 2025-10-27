package com.skytech.projectmanagement.user.dto;

import java.time.Instant;
import com.skytech.projectmanagement.user.entity.User;

public record UserResponse(Integer id, String fullName, String email, String avatarUrl,
        Instant createdAt, Boolean isProductOwner) {

    public static UserResponse fromEntity(User user) {

        return new UserResponse(user.getId(), user.getFullName(), user.getEmail(), user.getAvatar(),
                user.getCreatedAt(), user.getIsProductOwner());
    }
}
