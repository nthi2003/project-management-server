package com.skytech.projectmanagement.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RoleNameExistsException extends RuntimeException {
    public RoleNameExistsException(String message) {
        super(message);
    }
}
