package com.skytech.projectmanagement.teams.repository;

import com.skytech.projectmanagement.teams.entity.Teams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TeamsRepository extends JpaRepository<Teams, UUID> {
}
