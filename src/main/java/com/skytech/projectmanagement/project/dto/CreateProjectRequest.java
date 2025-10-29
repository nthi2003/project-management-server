package com.skytech.projectmanagement.project.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateProjectRequest(
        @NotBlank(message = "Tên dự án không được để trống") String projectName,

        @NotBlank(message = "Key dự án không được để trống") @Size(min = 3, max = 10,
                message = "Key dự án phải từ 3 đến 10 ký tự") @Pattern(regexp = "^[A-Z0-9]+$",
                        message = "Key dự án chỉ được chứa chữ hoa và số") String projectKey,

        String description, LocalDate dueDate) {

}
