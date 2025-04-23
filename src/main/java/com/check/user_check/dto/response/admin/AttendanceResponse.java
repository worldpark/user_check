package com.check.user_check.dto.response.admin;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AttendanceResponse(
        UUID attendanceId,
        String attendanceName
) {}
