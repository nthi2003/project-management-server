package com.skytech.projectmanagement.teams.repository;



import com.skytech.projectmanagement.teams.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    List<TeamMember> findByTeamId(UUID teamId);
    Optional<TeamMember> findByTeamIdAndUserId(UUID teamId, Integer userId);
}
