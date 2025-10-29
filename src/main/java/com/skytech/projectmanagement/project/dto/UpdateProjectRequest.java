package com.skytech.projectmanagement.project.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.Size;

public record UpdateProjectRequest(

        @Size(min = 1, message = "Tên dự án không được để trống") String projectName,

        String description,

        LocalDate dueDate) {
}
