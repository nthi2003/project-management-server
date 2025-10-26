package com.skytech.projectmanagement.user.repository;

import java.util.Optional;
import com.skytech.projectmanagement.user.entity.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Integer> {
    Optional<UserRefreshToken> findByRefreshToken(String refreshToken);

    void deleteByRefreshToken(String refreshToken);

    void deleteByUserId(Integer userId);
}
