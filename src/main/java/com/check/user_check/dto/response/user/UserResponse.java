package com.check.user_check.dto.response.user;

import com.check.user_check.enumeratedType.Role;
import lombok.Builder;

import java.util.UUID;

@Builder
public record UserResponse(UUID uid, String userId, String userName, Role role) {
}
