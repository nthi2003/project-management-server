package com.skytech.projectmanagement.project.dto;

import com.skytech.projectmanagement.project.entity.ProjectRole;
import jakarta.validation.constraints.NotNull;

public record AddMemberRequest(@NotNull(message = "User ID không được để trống") Integer userId,

        @NotNull(message = "Vai trò không được để trống") ProjectRole role) {

}
