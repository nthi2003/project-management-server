package com.skytech.projectmanagement.teams.service.impl;

import java.util.UUID;
import com.skytech.projectmanagement.common.exception.FileStorageException;
import com.skytech.projectmanagement.common.exception.ResourceNotFoundException;
import com.skytech.projectmanagement.filestorage.service.FileStorageService;
import com.skytech.projectmanagement.teams.dto.TeamsDTO;
import com.skytech.projectmanagement.teams.entity.Teams;
import com.skytech.projectmanagement.teams.mapper.TeamMapper;
import com.skytech.projectmanagement.teams.repository.TeamMemberRepository;
import com.skytech.projectmanagement.teams.repository.TeamsRepository;
import com.skytech.projectmanagement.teams.service.TeamService;
import com.skytech.projectmanagement.user.entity.User;
import com.skytech.projectmanagement.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamsRepository teamsRepository;
    private final TeamMapper teamMapper;
    private final TeamMemberRepository teamMemberRepository;
    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;

    @Override
    public Page<TeamsDTO> getAllTeams(Pageable pageable) {
        return teamsRepository.findAll(pageable).map(teamMapper::toDto);
    }

    @Override
    public TeamsDTO getTeamById(UUID id) {
        Teams team = teamsRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Không tìm thấy team với ID: " + id));
        return teamMapper.toDto(team);
    }

    @Override
    public TeamsDTO createTeam(TeamsDTO dto) {
        Teams team = teamMapper.toEntity(dto);

        Teams savedTeam = teamsRepository.save(team);
        return teamMapper.toDto(savedTeam);
    }

    @Override
    public TeamsDTO updateTeam(UUID id, TeamsDTO dto) {
        Teams team = teamsRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Không tìm thấy team với ID: " + id));

        team.setGroupName(dto.getGroupName());
        team.setDescription(dto.getDescription());
        team.setImageUrl(dto.getImageUrl());

        Teams updatedTeam = teamsRepository.save(team);
        return teamMapper.toDto(updatedTeam);
    }

    @Override
    public void deleteTeam(UUID id) {
        Teams team = teamsRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Không tìm thấy team với ID: " + id));
        teamsRepository.delete(team);
    }

    @Override
    public String uploadTeamAvatar(UUID teamId, MultipartFile file, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"));

        Teams team = teamsRepository.findById(teamId).orElseThrow(
                () -> new ResourceNotFoundException("Không tìm thấy team với ID: " + teamId));

        // if (!user.getIsProductOwner() && !checkUserInTeam(teamId, user.getId())) {
        // throw new RuntimeException("Bạn không có quyền cập nhật ảnh team này.");
        // }

        if (file.isEmpty() || file.getSize() > 5 * 1024 * 1024
                || (!file.getContentType().equals("image/jpeg")
                        && !file.getContentType().equals("image/png"))) {
            throw new FileStorageException("File không hợp lệ. Chỉ chấp nhận jpg/png và <5MB");
        }

        String objectName =
                "teams/" + teamId + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
        String fileUrl = fileStorageService.uploadFile(file, objectName);

        team.setImageUrl(fileUrl);
        teamsRepository.save(team);

        return fileUrl;
    }

    private boolean checkUserInTeam(UUID teamId, Integer userId) {
        return teamMemberRepository.findByTeamIdAndUserId(teamId, userId).isPresent();
    }
}
