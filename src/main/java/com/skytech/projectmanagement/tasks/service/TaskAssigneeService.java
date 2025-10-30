package com.skytech.projectmanagement.tasks.service;

import com.skytech.projectmanagement.tasks.dto.TaskAssigneeRequestDTO;
import com.skytech.projectmanagement.tasks.dto.TaskAssigneeResponseDTO;

import java.util.List;
import java.util.UUID;

public interface TaskAssigneeService {
    TaskAssigneeResponseDTO assignUserToTask(Integer teamId, Integer userId);
    void unassignUserFromTask(Integer taskId, Integer userId);
    List<TaskAssigneeResponseDTO> getAssigneesByTask(Integer taskId);
}
