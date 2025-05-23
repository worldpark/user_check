package com.check.user_check.dto.response.common;

import lombok.Builder;

import java.util.List;

@Builder
public record ListAttendanceResponse(
        List<AttendanceResponse> attendanceResponses
) {
}
