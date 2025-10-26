package com.skytech.projectmanagement.common.exception;

import javax.security.sasl.AuthenticationException;

public class RefreshTokenException extends AuthenticationException {
    public RefreshTokenException(String message) {
        super(message);
    }
}
