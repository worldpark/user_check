package com.check.user_check.dto.request.attendance;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AttendanceRequest(

        @NotNull(message = "관리자에게 문의해주세요.")
        @Schema(description = "출결 기록 식별자", example = "d72be5b0-2031-11f0-8337-bd8c4a471c40")
        UUID attendanceId,

        @NotNull(message = "올바르지 않은 계정입니다.")
        @Schema(description = "출석자 ID", example = "cf9c2760-36c5-11f0-9062-c5dcca459193")
        UUID attendanceUserName
) {}
