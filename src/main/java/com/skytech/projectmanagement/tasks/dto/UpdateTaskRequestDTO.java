package com.skytech.projectmanagement.tasks.dto;

import com.skytech.projectmanagement.tasks.entity.TaskMoscow;
import com.skytech.projectmanagement.tasks.entity.TaskPriority;
import com.skytech.projectmanagement.tasks.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskRequestDTO {
    @NotBlank(message = "Title cannot be blank")
    private String title;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    private TaskMoscow moscow;

    private LocalDate startDate;

    private LocalDate dueDate;

    private Instant completedAt;
}
