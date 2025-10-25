package com.skytech.projectmanagement.auth.controller;

import com.skytech.projectmanagement.auth.dto.LoginRequest;
import com.skytech.projectmanagement.auth.dto.LoginResponse;
import com.skytech.projectmanagement.auth.service.AuthService;
import com.skytech.projectmanagement.common.dto.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth-service/v1")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginData = authService.login(loginRequest);

        SuccessResponse<LoginResponse> response =
                SuccessResponse.of(loginData, "Đăng nhập thành công.");

        return ResponseEntity.ok(response);
    }
}
