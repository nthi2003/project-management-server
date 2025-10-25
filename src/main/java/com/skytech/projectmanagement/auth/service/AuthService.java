package com.skytech.projectmanagement.auth.service;

import com.skytech.projectmanagement.auth.dto.LoginRequest;
import com.skytech.projectmanagement.auth.dto.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest loginRequest);

}
