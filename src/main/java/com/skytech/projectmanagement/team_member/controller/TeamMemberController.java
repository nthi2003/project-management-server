package com.skytech.projectmanagement.team_member.controller;

import com.skytech.projectmanagement.common.dto.ErrorDetails;
import com.skytech.projectmanagement.common.dto.ErrorResponse;
import com.skytech.projectmanagement.common.dto.SuccessResponse;
import com.skytech.projectmanagement.team_member.dto.TeamMemberDTO;
import com.skytech.projectmanagement.team_member.entity.TeamMember;
import com.skytech.projectmanagement.team_member.mapper.TeamMemberMapper;
import com.skytech.projectmanagement.team_member.service.TeamMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/team-service/v1/members")
@RequiredArgsConstructor
public class TeamMemberController {
    private final TeamMemberService teamMemberService;
    private final TeamMemberMapper mapper;

    @GetMapping("/{teamId}/members")
    public ResponseEntity<?> getMembers(@PathVariable UUID teamId) {
        List<TeamMemberDTO> members = teamMemberService.getMembersByTeamId(teamId);
        return ResponseEntity.ok(SuccessResponse.of(members, "Lấy danh sách thành viên thành công."));
    }

    @PostMapping("/{teamId}/members/{userId}")
    @PreAuthorize("hasAuthority('ROLE_PRODUCT_OWNER')")
    public ResponseEntity<?> addMember(@PathVariable UUID teamId, @PathVariable Integer userId) {
        TeamMember member = teamMemberService.addMember(teamId, userId);
        TeamMemberDTO dto = mapper.toDto(member);
        return ResponseEntity.ok(SuccessResponse.of(dto, "Thêm thành viên vào team thành công."));
    }


    @DeleteMapping("/{teamId}/members/{userId}")
    @PreAuthorize("hasAuthority('ROLE_PRODUCT_OWNER')")
    public ResponseEntity<?> removeMember(@PathVariable UUID teamId, @PathVariable Integer userId) {
        teamMemberService.removeMember(teamId, userId);
        return ResponseEntity.ok(SuccessResponse.of(null, "Xóa thành viên khỏi team thành công."));
    }
}
