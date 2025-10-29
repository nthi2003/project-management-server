package com.skytech.projectmanagement.user.service;

import com.skytech.projectmanagement.common.dto.PaginatedResponse;
import com.skytech.projectmanagement.common.exception.InvalidOldPasswordException;
import com.skytech.projectmanagement.common.exception.ResourceNotFoundException;
import com.skytech.projectmanagement.user.dto.ChangePasswordRequest;
import com.skytech.projectmanagement.user.dto.CreateUserRequest;
import com.skytech.projectmanagement.user.dto.UpdateUserRequest;
import com.skytech.projectmanagement.user.dto.UserResponse;
import com.skytech.projectmanagement.user.entity.User;
import com.skytech.projectmanagement.user.repository.UserRefreshTokenRepository;
import com.skytech.projectmanagement.user.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Map<Integer, User> findUsersMapByIds(Set<Integer> userIds) {
        return null;
    }

    @Override
    public UserResponse uploadAvatar(String userEmail, MultipartFile file) {
        return null;
    }

    @Override
    public void deleteUser(Integer userId) {

    }

    @Override
    public UserResponse updateUser(Integer userId, UpdateUserRequest request) {
        return null;
    }

    @Override
    public UserResponse getUserById(Integer userId) {
        return null;
    }

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        return null;
    }

    @Override
    public PaginatedResponse<UserResponse> getUsers(Pageable pageable, String search) {
        return null;
    }

    @Override
    public UserResponse getUserProfile(String email) {
        return null;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(
                "Không tìm thấy người dùng với email: " + email));
    }

    @Override
    public User findUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Không tìm thấy người dùng với ID: " + id));
    }

    @Override
    @Transactional
    public void updatePassword(Integer userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));

        user.setHashPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void changePassword(String userEmail, ChangePasswordRequest request) {
        User currentUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng."));

        if (!passwordEncoder.matches(request.oldPassword(), currentUser.getHashPassword())) {
            throw new InvalidOldPasswordException(
                    "Mật khẩu cũ bạn đã nhập không khớp với mật khẩu hiện tại.");
        }

        currentUser.setHashPassword(passwordEncoder.encode(request.newPassword()));

        userRepository.save(currentUser);
        userRefreshTokenRepository.deleteByUserId(currentUser.getId());
    }

}
