package com.skytech.projectmanagement.user.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import com.skytech.projectmanagement.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository
        extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    List<User> findByIsAdmin(Boolean isAdmin);

    Optional<User> findByEmail(String email);

    List<User> findByIdIn(Collection<Integer> userIds);

    long countByIdIn(Collection<Integer> userIds);
}
