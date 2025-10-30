package com.skytech.projectmanagement.auth.dto;

import java.util.List;
import jakarta.validation.constraints.NotEmpty;

public record UpdateUserRolesRequest(
        @NotEmpty(message = "Danh sách role_ids không được rỗng") List<Integer> roleIds) {

}
