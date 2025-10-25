package com.skytech.projectmanagement.auth.service;

import java.time.Instant;

public interface RefreshTokenService {
    void saveRefreshToken(Integer userId, String token, Instant expiryDate, String deviceInfo);
}
