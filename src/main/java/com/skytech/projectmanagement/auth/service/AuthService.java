package com.skytech.projectmanagement.auth.service;

import com.skytech.projectmanagement.auth.dto.LoginRequest;
import com.skytech.projectmanagement.auth.dto.LoginResponse;
import com.skytech.projectmanagement.auth.dto.RefreshTokenRequest;
import com.skytech.projectmanagement.auth.dto.RefreshTokenResponse;
import com.skytech.projectmanagement.auth.dto.ResetPasswordRequest;

public interface AuthService {

    LoginResponse login(LoginRequest loginRequest);

    void logout(String refreshToken);

    RefreshTokenResponse refreshToken(RefreshTokenRequest request);

    void handleForgotPassword(String email);

    void resetPassword(ResetPasswordRequest request);
}
