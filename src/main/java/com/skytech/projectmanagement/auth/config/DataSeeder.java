package com.skytech.projectmanagement.auth.config;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.skytech.projectmanagement.auth.entity.Permission;
import com.skytech.projectmanagement.auth.repository.PermissionRepository;
import com.skytech.projectmanagement.user.entity.User;
import com.skytech.projectmanagement.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {
    @Value("${seeder.admin.email}")
    private String adminEmail;

    @Value("${seeder.admin.password}")
    private String adminPassword;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Bắt đầu quá trình seeding dữ liệu...");

        Map<String, Permission> permissions = new HashMap<>();

        permissions.put("USER_READ", findOrCreatePermission("USER_READ",
                "Quyền xem danh sách tất cả người dùng (GET /users)"));
        permissions.put("USER_MANAGE", findOrCreatePermission("USER_MANAGE",
                "Quyền Tạo/Sửa/Xóa bất kỳ người dùng nào (POST/PATCH/DELETE /users)"));
        permissions.put("ROLE_MANAGE", findOrCreatePermission("ROLE_MANAGE",
                "Quyền gán Role và Quyền trực tiếp cho user"));
        permissions.put("PROJECT_CREATE",
                findOrCreatePermission("PROJECT_CREATE", "Quyền tạo dự án mới (POST /projects)"));
        permissions.put("PROJECT_READ_ALL", findOrCreatePermission("PROJECT_READ_ALL",
                "Quyền xem TẤT CẢ dự án (bỏ qua filter \"thành viên\")"));
        permissions.put("PROJECT_MANAGE_ANY",
                findOrCreatePermission("PROJECT_MANAGE_ANY", "Quyền Sửa/Xóa BẤT KỲ dự án nào"));
        permissions.put("PROJECT_MEMBER_MANAGE", findOrCreatePermission("PROJECT_MEMBER_MANAGE",
                "Quyền Thêm/Sửa/Xóa thành viên khỏi dự án"));
        permissions.put("TASK_CREATE", findOrCreatePermission("TASK_CREATE", "Quyền tạo Task"));
        permissions.put("TASK_READ_ALL",
                findOrCreatePermission("TASK_READ_ALL", "Quyền xem tất cả Task (quyền PM/BA)"));
        permissions.put("BUG_CREATE", findOrCreatePermission("BUG_CREATE", "Quyền tạo Bug"));

        List<User> listUsers = userRepository.findByIsAdmin(true);

        if (listUsers.isEmpty()) {
            log.info("Tạo tài khoản Admin: {}", adminEmail);

            User adminUser = new User();
            adminUser.setEmail(adminEmail);
            adminUser.setFullName("Quản trị viên");
            adminUser.setHashPassword(passwordEncoder.encode(adminPassword));
            adminUser.setIsAdmin(true);
            adminUser.setCreatedAt(Instant.now());

            userRepository.save(adminUser);

            log.info("Tài khoản Admin đã được tạo.");

        } else {
            log.info("Tài khoản Admin ({}) đã tồn tại. Bỏ qua seeding.", adminEmail);
        }

        log.info("Quá trình seeding dữ liệu hoàn tất.");
    }

    private Permission findOrCreatePermission(String name, String description) {
        return permissionRepository.findByName(name).orElseGet(() -> {
            log.info("Tạo Permission: {}", name);
            Permission newPerm = new Permission();
            newPerm.setName(name);
            newPerm.setDescription(description);
            return permissionRepository.save(newPerm);
        });
    }

}
