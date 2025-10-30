package com.skytech.projectmanagement.auth.repository;

import com.skytech.projectmanagement.auth.entity.RolePermission;
import com.skytech.projectmanagement.auth.entity.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionId> {

    @Transactional
    void deleteById_RoleId(Integer roleId);
}
