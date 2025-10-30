package com.skytech.projectmanagement.auth.repository;

import com.skytech.projectmanagement.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

}
