package com.skytech.projectmanagement.tasks.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskAssigneeResponseDTO {
    private Long id;
    private Integer taskId;
    private Integer userId;
}
