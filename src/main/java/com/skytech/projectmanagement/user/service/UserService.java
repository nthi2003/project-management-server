package com.skytech.projectmanagement.user.service;

import java.util.Map;
import java.util.Set;
import com.skytech.projectmanagement.common.dto.PaginatedResponse;
import com.skytech.projectmanagement.user.dto.ChangePasswordRequest;
import com.skytech.projectmanagement.user.dto.CreateUserRequest;
import com.skytech.projectmanagement.user.dto.UpdateUserRequest;
import com.skytech.projectmanagement.user.dto.UserResponse;
import com.skytech.projectmanagement.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    boolean existsById(Integer userId);

    void validateUsersExist(Set<Integer> userIds);

    Map<Integer, User> findUsersMapByIds(Set<Integer> userIds);

    UserResponse uploadAvatar(String userEmail, MultipartFile file);

    void deleteUser(Integer userId);

    UserResponse updateUser(Integer userId, UpdateUserRequest request);

    UserResponse getUserById(Integer userId);

    UserResponse createUser(CreateUserRequest request);

    PaginatedResponse<UserResponse> getUsers(Pageable pageable, String search);

    UserResponse getUserProfile(String email);

    User findUserByEmail(String email);

    User findUserById(Integer id);

    void updatePassword(Integer userId, String newPassword);

    void changePassword(String userEmail, ChangePasswordRequest request);
    User getUserEntityById(Integer userId);

}
