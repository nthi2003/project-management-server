package com.skytech.projectmanagement.team_member.mapper;

import com.skytech.projectmanagement.team_member.dto.TeamMemberDTO;
import com.skytech.projectmanagement.team_member.entity.TeamMember;
import com.skytech.projectmanagement.teams.dto.TeamsDTO;
import com.skytech.projectmanagement.teams.entity.Teams;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TeamMemberMapper {
    @Mapping(source = "team.id", target = "teamId")
    @Mapping(source = "team.groupName", target = "teamName")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.fullName", target = "fullName")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.avatar", target = "avatar")
    @Mapping(source = "user.isProductOwner", target = "isProductOwner")
    TeamMemberDTO toDto(TeamMember teamMember);
    TeamMember toEntity(TeamMemberDTO teamMemberDTO);
}
