package com.skytech.projectmanagement.tasks.repository;

import com.skytech.projectmanagement.tasks.entity.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TaskRepository extends JpaRepository<Tasks, Integer>, JpaSpecificationExecutor<Tasks> {
}
