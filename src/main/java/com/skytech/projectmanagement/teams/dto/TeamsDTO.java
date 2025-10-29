package com.skytech.projectmanagement.teams.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamsDTO {
    private UUID id;
    private String groupName;
    private String description;
    private String imageUrl;
    private LocalDateTime createdAt;
}
