package com.skytech.projectmanagement.user.service;

import java.time.Instant;
import com.skytech.projectmanagement.common.exception.InvalidTokenException;
import com.skytech.projectmanagement.user.entity.UserRefreshToken;
import com.skytech.projectmanagement.user.repository.UserRefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final UserRefreshTokenRepository tokenRepository;

    @Override
    @Transactional
    public void saveRefreshToken(Integer userId, String refreshToken, Instant expiryDate,
            String deviceInfo) {
        UserRefreshToken userRefreshToken = new UserRefreshToken();
        userRefreshToken.setUserId(userId);
        userRefreshToken.setRefreshToken(refreshToken);
        userRefreshToken.setExpiresAt(expiryDate);
        userRefreshToken.setDeviceInfo(deviceInfo);

        tokenRepository.save(userRefreshToken);
    }

    @Override
    @Transactional
    public void deleteRefreshToken(String refreshToken) {
        UserRefreshToken tokenEntity = tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new InvalidTokenException(
                        "Refresh token không tìm thấy hoặc đã bị thu hồi."));

        tokenRepository.delete(tokenEntity);
    }

    @Override
    @Transactional
    public UserRefreshToken verifyAndRotateRefreshToken(String oldToken) {
        UserRefreshToken tokenEntity = tokenRepository.findByRefreshToken(oldToken)
                .orElseThrow(() -> new InvalidTokenException(
                        "Refresh token không tìm thấy hoặc đã bị thu hồi."));

        if (tokenEntity.getExpiresAt().isBefore(Instant.now())) {
            tokenRepository.delete(tokenEntity);
            throw new InvalidTokenException("Refresh token đã hết hạn.");
        }

        tokenRepository.delete(tokenEntity);

        return tokenEntity;
    }

}
