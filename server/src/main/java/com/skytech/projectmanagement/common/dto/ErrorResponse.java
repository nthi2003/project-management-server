package com.skytech.projectmanagement.common.dto;

public record ErrorResponse(boolean success, String message, ErrorDetails error) {

    public static ErrorResponse of(String message, ErrorDetails errorDetails) {
        return new ErrorResponse(false, message, errorDetails);
    }
}
