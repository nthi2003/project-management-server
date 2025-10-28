package com.skytech.projectmanagement.project.repository;

import com.skytech.projectmanagement.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProjectRepository
        extends JpaRepository<Project, Integer>, JpaSpecificationExecutor<Project> {

    boolean existsByProjectKeyIgnoreCase(String projectKey);
}
