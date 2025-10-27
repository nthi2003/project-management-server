package com.skytech.projectmanagement.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // 409
public class DeleteConflictException extends RuntimeException {
    public DeleteConflictException(String message) {
        super(message);
    }
}
