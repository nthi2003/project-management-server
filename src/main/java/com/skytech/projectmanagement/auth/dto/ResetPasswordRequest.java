package com.skytech.projectmanagement.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(@NotBlank(message = "Token không được để trống") String token,

        @NotBlank(message = "Mật khẩu mới không được để trống") @Size(min = 6,
                message = "Mật khẩu mới phải có ít nhất 6 ký tự") String newPassword) {

}
