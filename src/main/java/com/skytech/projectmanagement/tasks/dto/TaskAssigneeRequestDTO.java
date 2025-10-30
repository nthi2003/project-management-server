package com.skytech.projectmanagement.tasks.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskAssigneeRequestDTO {
    @NotNull(message = "Task ID không được để trống")
    private Integer taskId;

    @NotNull(message = "User ID không được để trống")
    private Integer userId;
}
