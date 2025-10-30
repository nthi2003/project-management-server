package com.skytech.projectmanagement.auth.controller;

import java.util.List;
import com.skytech.projectmanagement.auth.dto.PermissionResponse;
import com.skytech.projectmanagement.auth.service.RoleService;
import com.skytech.projectmanagement.common.dto.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth-service/v1/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final RoleService roleService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public ResponseEntity<SuccessResponse<List<PermissionResponse>>> getAllPermissions() {

        List<PermissionResponse> permissions = roleService.getAllPermissions();

        SuccessResponse<List<PermissionResponse>> response =
                SuccessResponse.of(permissions, "Lấy danh sách quyền thành công.");

        return ResponseEntity.ok(response);
    }
}
