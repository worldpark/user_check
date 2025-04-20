package com.check.user_check.dto.request;

import com.check.user_check.enumeratedType.Role;

public record UserUpdateRequest(String password, String userName, Role role) {}
