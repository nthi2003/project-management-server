package com.skytech.projectmanagement.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // 400
public class InvalidOldPasswordException extends RuntimeException {
    public InvalidOldPasswordException(String message) {
        super(message);
    }
}
