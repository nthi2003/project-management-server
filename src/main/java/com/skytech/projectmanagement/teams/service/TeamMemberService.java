package com.skytech.projectmanagement.teams.service;

import com.skytech.projectmanagement.teams.dto.TeamMemberDTO;
import com.skytech.projectmanagement.teams.entity.TeamMember;

import java.util.List;
import java.util.UUID;

public interface TeamMemberService {
    List<TeamMemberDTO> getMembersByTeamId(UUID teamId);
    TeamMember addMember(UUID teamId, Integer userId);
    void removeMember(UUID teamId, Integer userId);
}
