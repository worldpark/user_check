package com.check.user_check.dto.response.admin;


import com.check.user_check.enumeratedType.AttendanceAuth;

import java.util.UUID;

public record AttendanceTargetResponse(
        UUID uid,
        String userId,
        String userName,
        AttendanceAuth auth
) {
}
