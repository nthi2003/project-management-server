package com.skytech.projectmanagement.auth.dto;

public record UserLoginResponse(Integer id, String fullName, String email, String avatar,
        Boolean isProductOwner) {
}
