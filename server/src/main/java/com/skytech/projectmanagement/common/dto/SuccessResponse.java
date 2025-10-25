package com.skytech.projectmanagement.common.dto;

public record SuccessResponse<T>(boolean success, T data, String message) {

    public static <T> SuccessResponse<T> of(T data, String message) {
        return new SuccessResponse<>(true, data, message);
    }
}
