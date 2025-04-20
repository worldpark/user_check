package com.check.user_check.dto.response;

import com.check.user_check.dto.response.user.UserResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record UserListResponse(
        List<UserResponse> users
) {}
