package com.skytech.projectmanagement.user.service;

import com.skytech.projectmanagement.user.entity.User;

public interface UserService {

    User findUserByEmail(String email);

    User findUserById(Integer id);

    void updatePassword(Integer userId, String newPassword);
}
