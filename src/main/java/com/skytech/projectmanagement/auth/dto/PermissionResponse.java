package com.skytech.projectmanagement.auth.dto;

import com.skytech.projectmanagement.auth.entity.Permission;

public record PermissionResponse(Integer id, String name, String description) {

    public static PermissionResponse fromEntity(Permission permission) {
        return new PermissionResponse(permission.getId(), permission.getName(),
                permission.getDescription());
    }
}
