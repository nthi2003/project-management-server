package com.skytech.projectmanagement.auth.service;

import com.skytech.projectmanagement.auth.dto.LoginRequest;
import com.skytech.projectmanagement.auth.dto.LoginResponse;
import com.skytech.projectmanagement.auth.dto.UserLoginResponse;
import com.skytech.projectmanagement.auth.security.JwtTokenProvider;
import com.skytech.projectmanagement.common.exception.ResourceNotFoundException;
import com.skytech.projectmanagement.user.entity.User;
import com.skytech.projectmanagement.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final long jwtExpirationMs;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider, UserRepository userRepository,
            RefreshTokenService refreshTokenService,
            @Value("${jwt.expiration-ms}") long jwtExpirationMs) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.refreshTokenService = refreshTokenService;
        this.jwtExpirationMs = jwtExpirationMs;
    }

    @Override
    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.email(), loginRequest.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtTokenProvider.generateJwtToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng."));

        refreshTokenService.saveRefreshToken(user.getId(), refreshToken,
                jwtTokenProvider.getExpirationDateFromToken(refreshToken).toInstant(),
                loginRequest.deviceInfo());

        UserLoginResponse userResponse = new UserLoginResponse(user.getId(), user.getFullName(),
                user.getEmail(), user.getAvatar(), user.getIsProductOwner());

        return new LoginResponse(userResponse, accessToken, refreshToken, jwtExpirationMs / 1000);
    }

}
