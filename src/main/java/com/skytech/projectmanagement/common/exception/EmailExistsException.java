package com.skytech.projectmanagement.common.exception;

public class EmailExistsException extends RuntimeException{
    public EmailExistsException(String message) {
        super(message);
    }
}
