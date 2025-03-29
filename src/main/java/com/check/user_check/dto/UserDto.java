package com.check.user_check.dto;

import com.check.user_check.Role;
import lombok.Builder;

@Builder
public record UserDto(String userId, String userName, Role role) {
}
