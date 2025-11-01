package com.skytech.projectmanagement.user.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import com.skytech.projectmanagement.common.exception.InvalidResetTokenException;
import com.skytech.projectmanagement.user.entity.PasswordResetToken;
import com.skytech.projectmanagement.user.entity.User;
import com.skytech.projectmanagement.user.repository.PasswordResetTokenRepository;
import com.skytech.projectmanagement.user.service.PasswordResetTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private final PasswordResetTokenRepository tokenRepository;

    @Override
    @Transactional
    public String createResetToken(User user) {
        tokenRepository.deleteByUserId(user.getId());

        String token = UUID.randomUUID().toString();
        Instant expiresAt = Instant.now().plus(1, ChronoUnit.HOURS);

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUserId(user.getId());
        resetToken.setToken(token);
        resetToken.setExpiresAt(expiresAt);

        tokenRepository.save(resetToken);

        return token;
    }

    @Override
    @Transactional(noRollbackFor = InvalidResetTokenException.class)
    public PasswordResetToken validateResetToken(String token) {
        PasswordResetToken tokenEntity = tokenRepository.findByToken(token).orElseThrow(
                () -> new InvalidResetTokenException("Token đặt lại mật khẩu không tìm thấy."));

        if (tokenEntity.getExpiresAt().isBefore(Instant.now())) {
            tokenRepository.delete(tokenEntity);
            throw new InvalidResetTokenException("Token đặt lại mật khẩu đã hết hạn.");
        }

        return tokenEntity;
    }

    @Override
    @Transactional
    public void deleteToken(PasswordResetToken token) {
        tokenRepository.delete(token);
    }
}
