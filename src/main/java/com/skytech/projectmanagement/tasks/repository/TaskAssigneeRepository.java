package com.skytech.projectmanagement.tasks.repository;

import com.skytech.projectmanagement.tasks.entity.TaskAssignee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskAssigneeRepository extends JpaRepository<TaskAssignee, Long> {
    List<TaskAssignee> findByTaskId(Integer taskId);
    Optional<TaskAssignee> findByTaskIdAndUserId(Integer taskId, Integer userId);
    void deleteByTaskIdAndUserId(Integer taskId, Integer userId);
    boolean existsByTaskIdAndUserId(Integer taskId, Integer userId);

}
