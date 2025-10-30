package com.skytech.projectmanagement.tasks.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skytech.projectmanagement.tasks.entity.TaskMoscow;
import com.skytech.projectmanagement.tasks.entity.TaskPriority;
import com.skytech.projectmanagement.tasks.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class CreateTaskRequestDTO {
    @NotBlank(message = "Title không để trống")
    private String title;

    private String description;
    @NotNull(message = "Status không để trống")
    private TaskStatus status;
    private TaskPriority priority;
    private TaskMoscow moscow;
    private LocalDate startDate;

    private LocalDate dueDate;
    @NotNull(message = "Project ID không để trống")
    @JsonProperty("projectId")
    private Integer projectId;

    private Integer parentTaskId;
}
