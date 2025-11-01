package com.skytech.projectmanagement.tasks.service.impl;

import java.util.List;
import com.skytech.projectmanagement.common.exception.DeleteConflictException;
import com.skytech.projectmanagement.common.exception.ResourceNotFoundException;
import com.skytech.projectmanagement.tasks.dto.TaskAssigneeResponseDTO;
import com.skytech.projectmanagement.tasks.entity.TaskAssignee;
import com.skytech.projectmanagement.tasks.mapper.TaskAssigneeMapper;
import com.skytech.projectmanagement.tasks.repository.TaskAssigneeRepository;
import com.skytech.projectmanagement.tasks.service.TaskAssigneeService;
import com.skytech.projectmanagement.tasks.service.TaskService;
import com.skytech.projectmanagement.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskAssigneeServiceImpl implements TaskAssigneeService {
    private final TaskAssigneeRepository taskAssigneeRepository;
    private final UserService userService;
    private final TaskService taskService;
    private final TaskAssigneeMapper taskAssigneeMapper;


    @Override
    public TaskAssigneeResponseDTO assignUserToTask(Integer taskId, Integer userId) {
        userService.getUserById(userId);
        taskService.getTaskById(taskId);

        boolean exists = taskAssigneeRepository.existsByTaskIdAndUserId(taskId, userId);
        if (exists) {
            throw new DeleteConflictException("User này đã được gán cho task rồi");
        }

        TaskAssignee taskAssignee = new TaskAssignee();
        taskAssignee.setTaskId(taskId);
        taskAssignee.setUserId(userId);
        taskAssigneeRepository.save(taskAssignee);

        return taskAssigneeMapper.toDto(taskAssignee);

    }

    @Override
    public void unassignUserFromTask(Integer taskId, Integer userId) {
        boolean exists = taskAssigneeRepository.existsByTaskIdAndUserId(taskId, userId);
        if (!exists) {
            throw new ResourceNotFoundException("User chưa được gán cho task này");
        }
        taskAssigneeRepository.deleteByTaskIdAndUserId(taskId, userId);
    }

    @Override
    public List<TaskAssigneeResponseDTO> getAssigneesByTask(Integer taskId) {
        var list = taskAssigneeRepository.findByTaskId(taskId);
        return list.stream().map(taskAssigneeMapper::toDto).toList();
    }
}
