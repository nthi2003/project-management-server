package com.skytech.projectmanagement.auth.dto;

import com.skytech.projectmanagement.auth.entity.Role;

public record RoleResponse(Integer id, String name, String description) {

    public static RoleResponse fromEntity(Role role) {
        return new RoleResponse(role.getId(), role.getName(), role.getDescription());
    }
}
