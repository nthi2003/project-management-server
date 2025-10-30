package com.skytech.projectmanagement.auth.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_permissions")
@Getter
@Setter
@NoArgsConstructor
public class UserPermission {

    @EmbeddedId
    private UserPermissionId id;
}
