package com.skytech.projectmanagement.teams.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamMemberDTO {
    private Long id;          // ID của team_member

    private UUID teamId;      //  team dùng UUID
    private String teamName;  // để hiển thị nhanh mà không cần join

    private Integer userId;
    private String fullName;
    private String email;
    private String avatar;
    private Boolean isProductOwner;
}
