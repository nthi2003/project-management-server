package com.skytech.projectmanagement.teams.entity;

import com.skytech.projectmanagement.teams.entity.Teams;
import com.skytech.projectmanagement.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "team_members")
@Getter
@Setter
@NoArgsConstructor
public class TeamMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "team_id", nullable = false)
    private UUID teamId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;
}
