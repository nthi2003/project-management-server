package com.skytech.projectmanagement.teams.service;

import com.skytech.projectmanagement.teams.dto.TeamsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface TeamsService {
    Page<TeamsDTO> getAllTeams(Pageable pageable);
    TeamsDTO getTeamById(UUID id);
    TeamsDTO createTeam(TeamsDTO dto);
    TeamsDTO updateTeam(UUID id, TeamsDTO dto);
    void deleteTeam(UUID id);
    String uploadTeamAvatar(UUID teamId, MultipartFile file, String email);

}
