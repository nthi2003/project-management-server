package com.skytech.projectmanagement.project.repository;

import java.util.List;
import com.skytech.projectmanagement.project.entity.ProjectMember;
import com.skytech.projectmanagement.project.entity.ProjectMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {
    List<ProjectMember> findById_UserId(Integer userId);

    List<ProjectMember> findById_ProjectId(Integer projectId);

    boolean existsById_UserIdAndId_ProjectId(Integer userId, Integer projectId);

    @Transactional
    void deleteById_ProjectId(Integer projectId);

    boolean existsById_ProjectId(Integer projectId);
}
