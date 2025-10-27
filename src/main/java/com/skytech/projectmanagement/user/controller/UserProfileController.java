package com.skytech.projectmanagement.user.controller;

import com.skytech.projectmanagement.common.dto.SuccessResponse;
import com.skytech.projectmanagement.user.dto.ChangePasswordRequest;
import com.skytech.projectmanagement.user.dto.UserResponse;
import com.skytech.projectmanagement.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user-service/v1/me")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserService userService;

    @PostMapping("/avatar")
    public ResponseEntity<SuccessResponse<UserResponse>> uploadAvatar(
            @RequestParam("avatar") MultipartFile file) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();

        UserResponse updatedUserDto = userService.uploadAvatar(userEmail, file);

        SuccessResponse<UserResponse> response =
                SuccessResponse.of(updatedUserDto, "Cập nhật avatar thành công.");

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<UserResponse>> getMyProfile() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();

        UserResponse userDto = userService.getUserProfile(userEmail);

        SuccessResponse<UserResponse> response =
                SuccessResponse.of(userDto, "Lấy thông tin người dùng thành công.");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/change-password")
    public ResponseEntity<SuccessResponse<Object>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userService.changePassword(auth.getName(), request);

        return ResponseEntity.ok(SuccessResponse.of(null, "Thay đổi mật khẩu thành công."));
    }
}
