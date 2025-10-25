package com.skytech.projectmanagement.auth.service;

import java.time.Instant;

import org.bouncycastle.crypto.generators.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.skytech.projectmanagement.auth.security.JwtTokenProvider;
import com.skytech.projectmanagement.user.entity.UserRefreshToken;
import com.skytech.projectmanagement.user.repository.UserRefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final UserRefreshTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void saveRefreshToken(Integer userId, String token, Instant expiryDate, String deviceInfo) {
        UserRefreshToken refreshToken = new UserRefreshToken();
        refreshToken.setUserId(userId);
        refreshToken.setHashToken(passwordEncoder.encode(token));
        refreshToken.setExpiresAt(expiryDate);
        refreshToken.setDeviceInfo(deviceInfo);

        tokenRepository.save(refreshToken);
    }

}
