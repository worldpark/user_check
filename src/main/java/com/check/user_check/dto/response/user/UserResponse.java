package com.check.user_check.dto.response.user;

import com.check.user_check.enumeratedType.Role;
import lombok.Builder;

import java.util.UUID;

@Builder
public record UserResponse(UUID userId, String username, String name, Role role) {
}
