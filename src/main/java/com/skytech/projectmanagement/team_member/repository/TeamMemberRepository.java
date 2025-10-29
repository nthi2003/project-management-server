package com.skytech.projectmanagement.team_member.repository;

import com.skytech.projectmanagement.team_member.entity.TeamMember;
import com.skytech.projectmanagement.teams.entity.Teams;
import com.skytech.projectmanagement.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    List<TeamMember> findByTeamId(UUID teamId);
    Optional<TeamMember> findByTeamAndUser(Teams team, User user);
    void deleteByTeamAndUser(Teams team, User user);
    Optional<TeamMember> findByTeamIdAndUserId(UUID teamId, Integer userId);
}
