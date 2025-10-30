package com.skytech.projectmanagement.auth.dto;

import java.util.List;
import jakarta.validation.constraints.NotNull;

public record SyncRolePermissionsRequest(
        @NotNull(message = "Danh sách ID quyền không được rỗng") List<Integer> permissionIds) {
}
