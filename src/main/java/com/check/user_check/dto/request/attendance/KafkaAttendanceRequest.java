package com.check.user_check.dto.request.attendance;

import com.check.user_check.enumeratedType.AttendanceStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record KafkaAttendanceRequest(
        UUID attendanceId,
        LocalDateTime attendanceDate,
        LocalDateTime checkTime,
        AttendanceStatus status,
        String memo,
        UUID userId
) {
}
