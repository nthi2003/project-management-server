package com.skytech.projectmanagement.auth.repository;

import java.util.Collection;
import java.util.List;
import com.skytech.projectmanagement.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    long countByIdIn(Collection<Integer> ids);

    List<Role> findByIdIn(Collection<Integer> ids);

    boolean existsByNameIgnoreCase(String name);
}
