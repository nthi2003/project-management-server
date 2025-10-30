package com.skytech.projectmanagement.teams.controller;

import com.skytech.projectmanagement.auth.security.JwtTokenProvider;
import com.skytech.projectmanagement.common.dto.PaginatedResponse;
import com.skytech.projectmanagement.common.dto.Pagination;
import com.skytech.projectmanagement.common.dto.SuccessResponse;
import com.skytech.projectmanagement.teams.dto.TeamsDTO;
import com.skytech.projectmanagement.teams.service.TeamsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/teams-service/v1/teams")
@RequiredArgsConstructor
public class TeamsController {

    private final TeamsService teamsService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_PRODUCT_OWNER')")
    public ResponseEntity<PaginatedResponse<TeamsDTO>> getAllTeams(Pageable pageable) {
        Page<TeamsDTO> page = teamsService.getAllTeams(pageable);
        Pagination pagination = new Pagination(page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
        return ResponseEntity.ok(
                PaginatedResponse.of(page.getContent(), pagination, "Lấy danh sách team thành công.")
        );    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<TeamsDTO>> getTeamById(@PathVariable UUID id) {
        TeamsDTO team = teamsService.getTeamById(id);
        return ResponseEntity.ok(SuccessResponse.of(team, "Lấy thông tin team thành công."));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_PRODUCT_OWNER')")
    public ResponseEntity<SuccessResponse<TeamsDTO>> createTeam(@RequestBody TeamsDTO dto) {
        TeamsDTO team = teamsService.createTeam(dto);
        return ResponseEntity.ok(SuccessResponse.of(team, "Tạo team thành công."));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<TeamsDTO>> updateTeam(@PathVariable UUID id, @RequestBody TeamsDTO dto) {
        TeamsDTO updatedTeam = teamsService.updateTeam(id, dto);
        return ResponseEntity.ok(SuccessResponse.of(updatedTeam, "Cập nhật team thành công."));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_PRODUCT_OWNER')")
    public ResponseEntity<SuccessResponse<Void>> deleteTeam(@PathVariable UUID id) {
        teamsService.deleteTeam(id);
        return ResponseEntity.ok(SuccessResponse.of(null, "Xoá team thành công."));
    }

    @PostMapping("/{teamId}/avatar")
    public ResponseEntity<SuccessResponse<TeamsDTO>> uploadTeamAvatar(@PathVariable UUID teamId,
                                                                      @RequestParam("image") MultipartFile file,
                                                                      @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        String email = jwtTokenProvider.getEmail(token);

        teamsService.uploadTeamAvatar(teamId, file, email);

        TeamsDTO team = teamsService.getTeamById(teamId);
        return ResponseEntity.ok(SuccessResponse.of(team, "Cập nhật ảnh đại diện team thành công."));
    }
}
