package com.skytech.projectmanagement.tasks.dto;

import com.skytech.projectmanagement.tasks.entity.TaskMoscow;
import com.skytech.projectmanagement.tasks.entity.TaskPriority;
import com.skytech.projectmanagement.tasks.entity.TaskStatus;
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
public class TaskResponseDTO {
    private Integer id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private TaskMoscow moscow;
    private LocalDate startDate;
    private LocalDate dueDate;
    private Instant completedAt;
    private Instant createdAt;
    private Instant updatedAt;

    private Integer projectId;
    private String projectName;

    private Integer creatorId;
    private String creatorName;

    private Integer parentTaskId;
}
