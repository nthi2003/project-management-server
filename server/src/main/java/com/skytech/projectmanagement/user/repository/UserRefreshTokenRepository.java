package com.skytech.projectmanagement.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skytech.projectmanagement.user.entity.UserRefreshToken;

public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Integer> {

}
