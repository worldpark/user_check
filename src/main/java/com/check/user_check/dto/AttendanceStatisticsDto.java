package com.check.user_check.dto;

import com.check.user_check.enumeratedType.AttendanceStatus;

public record AttendanceStatisticsDto(
        AttendanceStatus attendanceStatus,
        Long count) {
}
