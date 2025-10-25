package com.skytech.projectmanagement.auth.dto;

public record LoginResponse(UserLoginResponse user, String accessToken, String refreshToken,
        long expiresIn) {

}
