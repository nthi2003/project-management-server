package com.skytech.projectmanagement.common.dto;

public record Pagination(int currentPage, int pageSize, long totalItems, int totalPages) {

}
