package com.skytech.projectmanagement.project.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import com.skytech.projectmanagement.project.entity.Project;

public record ProjectDetailsResponse(Integer id, String projectName, String projectKey,
        String description, LocalDate dueDate, Instant createdAt,
        List<ProjectMemberResponse> members) {

    public static ProjectDetailsResponse fromEntity(Project project,
            List<ProjectMemberResponse> members) {
        return new ProjectDetailsResponse(project.getId(), project.getProjectName(),
                project.getProjectKey(), project.getDescription(), project.getDueDate(),
                project.getCreatedAt(), members);
    }
}
