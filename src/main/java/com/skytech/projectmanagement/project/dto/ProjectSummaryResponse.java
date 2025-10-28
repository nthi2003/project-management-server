package com.skytech.projectmanagement.project.dto;

import java.time.Instant;
import java.time.LocalDate;
import com.skytech.projectmanagement.project.entity.Project;

public record ProjectSummaryResponse(Integer id, String projectName, String projectKey,
        String description, LocalDate dueDate, Instant createdAt) {

    public static ProjectSummaryResponse fromEntity(Project project) {
        return new ProjectSummaryResponse(project.getId(), project.getProjectName(),
                project.getProjectKey(), project.getDescription(), project.getDueDate(),
                project.getCreatedAt());
    }

}
