package com.check.user_check.dto.request.attendance;

import com.check.user_check.enumeratedType.AttendanceStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AttendanceUpdateRequest(

        @NotNull(message = "날짜 정보가 정확하지 않습니다.")
        @Schema(description = "출석 시간", example = "2025-05-05 08:00:00")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime checkTime,

        @Schema(description = "출석 상태", example = "PRESENT")
        AttendanceStatus status
) {}
