package com.skytech.projectmanagement.common.dto;

import java.util.List;

public record PaginatedResponse<T>(boolean success, List<T> data, Pagination pagination) {

    public static <T> PaginatedResponse<T> of(List<T> data, Pagination pagination) {
        return new PaginatedResponse<>(true, data, pagination);
    }

}
