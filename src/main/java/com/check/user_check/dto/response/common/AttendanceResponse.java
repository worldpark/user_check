package com.check.user_check.dto.response.common;

import com.check.user_check.enumeratedType.AttendanceStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record AttendanceResponse(
        UUID attendanceId,
        LocalDateTime attendanceDate,
        LocalDateTime checkTime,
        AttendanceStatus status,
        UUID userId,
        String username
) {}
