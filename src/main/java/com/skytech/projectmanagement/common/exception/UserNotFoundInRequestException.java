package com.skytech.projectmanagement.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // 400
public class UserNotFoundInRequestException extends RuntimeException {
    public UserNotFoundInRequestException(String message) {
        super(message);
    }
}
