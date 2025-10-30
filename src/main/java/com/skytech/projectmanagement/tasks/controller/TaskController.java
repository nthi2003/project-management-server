package com.skytech.projectmanagement.tasks.controller;

import com.skytech.projectmanagement.common.dto.SuccessResponse;
import com.skytech.projectmanagement.tasks.dto.CreateTaskRequestDTO;
import com.skytech.projectmanagement.tasks.dto.TaskResponseDTO;
import com.skytech.projectmanagement.tasks.dto.UpdateTaskRequestDTO;
import com.skytech.projectmanagement.tasks.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks-service/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<TaskResponseDTO>> getTaskById(@PathVariable("id") Integer taskId) {
        TaskResponseDTO task = taskService.getTaskById(taskId);
        return ResponseEntity.ok(SuccessResponse.of(task,"Lấy task thành công"));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_PRODUCT_OWNER')")
    public ResponseEntity<SuccessResponse<TaskResponseDTO>> createTask(
            @Valid @RequestBody CreateTaskRequestDTO requestDTO,
            Authentication authentication) {

        TaskResponseDTO createdTask = taskService.createTask(requestDTO, authentication);

        return ResponseEntity.ok(SuccessResponse.of(createdTask, "Tạo task thành công"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<TaskResponseDTO>> updateTask(@PathVariable("id") Integer taskId, @Valid @RequestBody UpdateTaskRequestDTO requestDTO) {
        TaskResponseDTO updatedTask = taskService.updateTask(taskId, requestDTO);
        return ResponseEntity.ok(SuccessResponse.of(updatedTask,"Cập nhật task thành công"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_PRODUCT_OWNER')")
    public ResponseEntity<SuccessResponse<Void>> deleteTask(@PathVariable("id") Integer taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok(SuccessResponse.of(null,"Xóa task thành công"));
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<List<TaskResponseDTO>>> getTasks(
            @RequestParam(required = false) Integer projectId,
            @RequestParam(required = false) Integer assigneeId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority
    ) {
        List<TaskResponseDTO> tasks = taskService.getTasks(projectId, assigneeId, status, priority);
        return ResponseEntity.ok(SuccessResponse.of(tasks, "Lấy danh sách task thành công"));
    }

}
