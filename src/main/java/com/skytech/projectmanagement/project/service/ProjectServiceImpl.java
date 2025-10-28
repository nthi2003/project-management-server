package com.skytech.projectmanagement.project.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import com.skytech.projectmanagement.common.dto.PaginatedResponse;
import com.skytech.projectmanagement.common.dto.Pagination;
import com.skytech.projectmanagement.common.exception.ProjectKeyExistsException;
import com.skytech.projectmanagement.common.exception.ResourceNotFoundException;
import com.skytech.projectmanagement.project.dto.CreateProjectRequest;
import com.skytech.projectmanagement.project.dto.ProjectDetailsResponse;
import com.skytech.projectmanagement.project.dto.ProjectMemberResponse;
import com.skytech.projectmanagement.project.dto.ProjectSummaryResponse;
import com.skytech.projectmanagement.project.dto.UpdateProjectRequest;
import com.skytech.projectmanagement.project.entity.Project;
import com.skytech.projectmanagement.project.entity.ProjectMember;
import com.skytech.projectmanagement.project.entity.ProjectMemberId;
import com.skytech.projectmanagement.project.entity.ProjectRole;
import com.skytech.projectmanagement.project.repository.ProjectMemberRepository;
import com.skytech.projectmanagement.project.repository.ProjectRepository;
import com.skytech.projectmanagement.user.entity.User;
import com.skytech.projectmanagement.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final UserService userService;

    @Override
    public PaginatedResponse<ProjectSummaryResponse> getProjects(Pageable pageable, String search,
            Authentication authentication) {

        Specification<Project> searchSpec = createSearchSpecification(search);

        Specification<Project> authSpec = createAuthorizationSpecification(authentication);

        Specification<Project> finalSpec = Specification.where(searchSpec).and(authSpec);

        Page<Project> projectPage = projectRepository.findAll(finalSpec, pageable);

        List<ProjectSummaryResponse> dtoList =
                projectPage.stream().map(ProjectSummaryResponse::fromEntity).toList();
        Pagination pagination = new Pagination(projectPage.getNumber(), projectPage.getSize(),
                projectPage.getTotalElements(), projectPage.getTotalPages());
        return PaginatedResponse.of(dtoList, pagination, "Lấy danh sách dự án thành công.");

    }

    @Override
    public ProjectDetailsResponse getProjectDetails(Integer projectId,
            Authentication authentication) {

        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new ResourceNotFoundException("Không tìm thấy dự án với ID: " + projectId));

        checkProjectAccess(projectId, authentication);

        List<ProjectMember> members = projectMemberRepository.findById_ProjectId(projectId);

        List<ProjectMemberResponse> memberResponses = new ArrayList<>();

        if (!members.isEmpty()) {
            Set<Integer> userIds = members.stream().map(member -> member.getId().getUserId())
                    .collect(Collectors.toSet());

            Map<Integer, User> userMap = userService.findUsersMapByIds(userIds);

            memberResponses = members.stream().map(member -> {
                User user = userMap.get(member.getId().getUserId());
                String name = (user != null) ? user.getFullName() : "Người dùng không tồn tại";
                String avatar = (user != null) ? user.getAvatar() : null;

                return new ProjectMemberResponse(member.getId().getUserId(), name, avatar,
                        member.getRole());
            }).toList();
        }

        return ProjectDetailsResponse.fromEntity(project, memberResponses);
    }

    @Override
    public void deleteProject(Integer projectId, Authentication authentication) {
        String email = authentication.getName();
        User currentUser = userService.findUserByEmail(email);

        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new ResourceNotFoundException("Không tìm thấy dự án với ID: " + projectId));

        checkAdminOrCreatorPermission(project, currentUser, authentication);

        projectMemberRepository.deleteById_ProjectId(projectId);

        projectRepository.delete(project);
    }

    private void checkAdminOrCreatorPermission(Project project, User currentUser,
            Authentication authentication) {
        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

        if (roles.contains("ROLE_PRODUCT_OWNER")) {
            return;
        }

        if (Objects.equals(project.getCreatedBy(), currentUser.getId())) {
            return;
        }

        throw new AccessDeniedException("Bạn không có quyền thực hiện hành động này.");
    }

    private void checkProjectAccess(Integer projectId, Authentication authentication) {
        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

        if (roles.contains("ROLE_PRODUCT_OWNER")) {
            return;
        }

        String email = authentication.getName();
        User currentUser = userService.findUserByEmail(email);

        boolean isMember = projectMemberRepository
                .existsById_UserIdAndId_ProjectId(currentUser.getId(), projectId);

        if (!isMember) {
            throw new AccessDeniedException("Bạn không có quyền xem dự án này.");
        }
    }

    private Specification<Project> createSearchSpecification(String search) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(search)) {
                return cb.conjunction();
            }
            String lowerCaseSearch = "%" + search.toLowerCase() + "%";
            Predicate nameLike = cb.like(cb.lower(root.get("projectName")), lowerCaseSearch);
            Predicate keyLike = cb.like(cb.lower(root.get("projectKey")), lowerCaseSearch);
            return cb.or(nameLike, keyLike);
        };
    }

    private Specification<Project> createAuthorizationSpecification(Authentication authentication) {
        // Lấy danh sách quyền của user
        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

        if (roles.contains("ROLE_PRODUCT_OWNER")) {
            return (root, query, cb) -> cb.conjunction();
        }

        return (root, query, cb) -> {
            String email = authentication.getName();
            User currentUser = userService.findUserByEmail(email);

            List<ProjectMember> memberships =
                    projectMemberRepository.findById_UserId(currentUser.getId());

            if (memberships.isEmpty()) {
                return cb.disjunction();
            }

            List<Integer> projectIds =
                    memberships.stream().map(pm -> pm.getId().getProjectId()).toList();

            return root.get("id").in(projectIds);
        };
    }

    @Override
    @Transactional
    public ProjectSummaryResponse createProject(CreateProjectRequest request,
            Authentication authentication) {

        if (projectRepository.existsByProjectKeyIgnoreCase(request.projectKey())) {
            throw new ProjectKeyExistsException(
                    "Project key '" + request.projectKey() + "' đã được sử dụng.");
        }

        String email = authentication.getName();
        User currentUser = userService.findUserByEmail(email);

        Project newProject = new Project();
        newProject.setProjectName(request.projectName());
        newProject.setProjectKey(request.projectKey().toUpperCase());
        newProject.setDescription(request.description());
        newProject.setDueDate(request.dueDate());
        newProject.setCreatedBy(currentUser.getId());

        Project savedProject = projectRepository.save(newProject);

        // Tự động thêm người tạo làm thành viên (ví dụ: PM)
        ProjectMember creatorAsMember = new ProjectMember();

        ProjectMemberId projectMemberId = new ProjectMemberId();
        projectMemberId.setProjectId(savedProject.getId());
        projectMemberId.setUserId(currentUser.getId());

        creatorAsMember.setId(projectMemberId);
        creatorAsMember.setRole(ProjectRole.PM);
        projectMemberRepository.save(creatorAsMember);

        return ProjectSummaryResponse.fromEntity(savedProject);
    }

    @Override
    public ProjectSummaryResponse updateProject(Integer projectId, UpdateProjectRequest request,
            Authentication authentication) {

        User currentUser = userService.findUserByEmail(authentication.getName());

        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new ResourceNotFoundException("Không tìm thấy dự án với ID: " + projectId));

        checkAdminOrCreatorPermission(project, currentUser, authentication);

        if (request.projectName() != null) {
            project.setProjectName(request.projectName());
        }
        if (request.description() != null) {
            project.setDescription(request.description());
        }
        if (request.dueDate() != null) {
            project.setDueDate(request.dueDate());
        }

        Project updatedProject = projectRepository.save(project);
        return ProjectSummaryResponse.fromEntity(updatedProject);
    }
}
