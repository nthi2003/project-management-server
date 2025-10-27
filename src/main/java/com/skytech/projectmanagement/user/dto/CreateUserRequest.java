package com.skytech.projectmanagement.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank(message = "Tên đầy đủ không được để trống") String fullName,

        @NotBlank(message = "Email không được để trống") @Email(
                message = "Email không đúng định dạng") String email,

        @NotBlank(message = "Mật khẩu không được để trống") @Size(min = 6,
                message = "Mật khẩu phải có ít nhất 6 ký tự") String password) {

}
