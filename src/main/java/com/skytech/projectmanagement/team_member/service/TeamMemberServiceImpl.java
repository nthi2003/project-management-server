package com.skytech.projectmanagement.team_member.service;

import com.skytech.projectmanagement.common.exception.DeleteConflictException;
import com.skytech.projectmanagement.common.exception.ResourceNotFoundException;
import com.skytech.projectmanagement.team_member.dto.TeamMemberDTO;
import com.skytech.projectmanagement.team_member.entity.TeamMember;
import com.skytech.projectmanagement.team_member.mapper.TeamMemberMapper;
import com.skytech.projectmanagement.team_member.repository.TeamMemberRepository;
import com.skytech.projectmanagement.teams.entity.Teams;
import com.skytech.projectmanagement.teams.repository.TeamsRepository;
import com.skytech.projectmanagement.user.entity.User;
import com.skytech.projectmanagement.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamMemberServiceImpl implements TeamMemberService{
    private final TeamMemberRepository teamMemberRepository;
    private final TeamsRepository teamsRepository;
    private final UserRepository userRepository;
    private final TeamMemberMapper mapper;

    @Override
    public List<TeamMemberDTO> getMembersByTeamId(UUID teamId) {
        Teams team = teamsRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy team với ID '" + teamId + "'"));

        List<TeamMember> members = teamMemberRepository.findByTeamId(teamId);

        return members.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }


    @Override
    public TeamMember addMember(UUID teamId, Integer userId) {
        Teams team = teamsRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team không tồn tại"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));

        if (teamMemberRepository.findByTeamAndUser(team, user).isPresent()) {
            throw new DeleteConflictException("Người dùng đã là thành viên của team này");
        }

        TeamMember member = new TeamMember();
        member.setTeam(team);
        member.setUser(user);
        return teamMemberRepository.save(member);
    }

    @Override
    public void removeMember(UUID teamId, Integer userId) {
        Teams team = teamsRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team không tồn tại"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));

        boolean exists = teamMemberRepository.findByTeamAndUser(team, user).isPresent();
        if (!exists) {
            throw new ResourceNotFoundException("Người dùng không phải thành viên của team");
        }

        teamMemberRepository.deleteByTeamAndUser(team, user);
    }
}
