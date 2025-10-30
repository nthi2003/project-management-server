package com.skytech.projectmanagement.auth.service;

import java.util.List;
import com.skytech.projectmanagement.auth.dto.PermissionResponse;
import com.skytech.projectmanagement.auth.dto.RoleResponse;
import com.skytech.projectmanagement.auth.dto.SyncRolePermissionsRequest;

public interface RoleService {

    List<RoleResponse> getAllRoles();

    List<PermissionResponse> getAllPermissions();

    List<PermissionResponse> getPermissionsByRoleId(Integer roleId);

    List<PermissionResponse> syncPermissionsForRole(Integer roleId,
            SyncRolePermissionsRequest request);
}
