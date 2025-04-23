package com.check.user_check.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record AttendanceUpdateRequest(

        @NotNull(message = "관리자에게 문의해주세요.")
        @Schema(description = "출결 아이디", example = "d72be5b0-2031-11f0-8337-bd8c4a471c40")
        UUID attendanceId,

        @NotNull()
        @Schema(description = "출결 이름", example = "과학 시험")
        @Size(max = 20, min = 5, message = "출결 이릉은 5글자이상 20글자 이하로만 입력 가능합니다.")
        String attendanceName
) {}
