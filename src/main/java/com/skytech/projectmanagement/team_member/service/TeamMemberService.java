package com.skytech.projectmanagement.team_member.service;

import com.skytech.projectmanagement.team_member.dto.TeamMemberDTO;
import com.skytech.projectmanagement.team_member.entity.TeamMember;

import java.util.List;
import java.util.UUID;

public interface TeamMemberService {
    List<TeamMemberDTO> getMembersByTeamId(UUID teamId);
    TeamMember addMember(UUID teamId, Integer userId);
    void removeMember(UUID teamId, Integer userId);
}
