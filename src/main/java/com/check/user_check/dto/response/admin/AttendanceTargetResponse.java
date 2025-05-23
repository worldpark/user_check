package com.check.user_check.dto.response.admin;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record AttendanceTargetResponse(
        UUID targetId,
        LocalDateTime attendanceDate,
        LocalDateTime createAt,
        String assignedUsername,
        String username
) {
}
