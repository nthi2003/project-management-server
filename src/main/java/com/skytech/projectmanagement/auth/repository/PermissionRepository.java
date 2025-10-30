package com.skytech.projectmanagement.auth.repository;

import java.util.Set;
import com.skytech.projectmanagement.auth.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    @Query(value = """
                SELECT p.name FROM permissions p
                JOIN role_permissions rp ON p.id = rp.permission_id
                JOIN user_roles ur ON rp.role_id = ur.role_id
                WHERE ur.user_id = :userId
                UNION
                SELECT p.name FROM permissions p
                JOIN user_permissions up ON p.id = up.permission_id
                WHERE up.user_id = :userId
            """, nativeQuery = true)
    Set<String> findAllPermissionsByUserId(@Param("userId") Integer userId);
}
