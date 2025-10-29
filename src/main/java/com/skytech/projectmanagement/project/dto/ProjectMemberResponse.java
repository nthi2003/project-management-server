package com.skytech.projectmanagement.project.dto;

import java.time.Instant;
import com.skytech.projectmanagement.project.entity.ProjectRole;

public record ProjectMemberResponse(Integer userId, String fullName, String avatarUrl,
        ProjectRole role, Instant joinedAt) {

}
