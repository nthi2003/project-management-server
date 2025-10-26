package com.skytech.projectmanagement.auth.dto;

public record RefreshTokenResponse(String accessToken, String refreshToken, long expiresIn) {

}
