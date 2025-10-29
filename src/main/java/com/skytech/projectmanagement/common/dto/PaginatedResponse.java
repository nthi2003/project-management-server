package com.skytech.projectmanagement.common.dto;

import java.util.List;

public record PaginatedResponse<T>(boolean success, String message, List<T> data,
        Pagination pagination) {

    public static <T> PaginatedResponse<T> of(List<T> data, Pagination pagination, String message) {
        return new PaginatedResponse<>(true, message, data, pagination);
    }

}
