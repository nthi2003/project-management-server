package com.skytech.projectmanagement.project.service;

import com.skytech.projectmanagement.common.dto.PaginatedResponse;
import com.skytech.projectmanagement.project.dto.CreateProjectRequest;
import com.skytech.projectmanagement.project.dto.ProjectDetailsResponse;
import com.skytech.projectmanagement.project.dto.ProjectSummaryResponse;
import com.skytech.projectmanagement.project.dto.UpdateProjectRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface ProjectService {

    ProjectSummaryResponse updateProject(Integer projectId, UpdateProjectRequest request,
            Authentication authentication);

    ProjectSummaryResponse createProject(CreateProjectRequest request,
            Authentication authentication);

    void deleteProject(Integer projectId, Authentication authentication);

    ProjectDetailsResponse getProjectDetails(Integer projectId, Authentication authentication);

    PaginatedResponse<ProjectSummaryResponse> getProjects(Pageable pageable, String search,
            Authentication authentication);
}
