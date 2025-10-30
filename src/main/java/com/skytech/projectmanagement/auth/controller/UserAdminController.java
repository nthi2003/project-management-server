package com.skytech.projectmanagement.auth.controller;

import java.util.List;
import com.skytech.projectmanagement.auth.dto.RoleResponse;
import com.skytech.projectmanagement.auth.dto.UpdateUserRolesRequest;
import com.skytech.projectmanagement.auth.service.RoleService;
import com.skytech.projectmanagement.common.dto.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth-service/v1/admin/users")
@RequiredArgsConstructor
public class UserAdminController {

    private final RoleService roleService;

    @PutMapping("/{userId}/roles")
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public ResponseEntity<SuccessResponse<List<RoleResponse>>> syncRolesForUser(
            @PathVariable Integer userId, @Valid @RequestBody UpdateUserRolesRequest request) {

        List<RoleResponse> updatedRoles = roleService.syncRolesForUser(userId, request);

        SuccessResponse<List<RoleResponse>> response =
                SuccessResponse.of(updatedRoles, "Cập nhật vai trò cho user thành công.");

        return ResponseEntity.ok(response);
    }
}
