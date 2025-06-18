package com.check.user_check.dto.response.common;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AttendanceResponse(
        UUID attendanceId,
        String attendanceDate,
        String checkTime,
        String status,
        UUID userId,
        String username,
        String name
) {}
