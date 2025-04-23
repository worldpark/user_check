package com.check.user_check.dto.request.target;

import com.check.user_check.enumeratedType.AttendanceAuth;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TargetCreateRequest(

        @Schema(description = "대상자 고유 ID", example = "d72be5b0-2031-11f0-8337-bd8c4a471c40")
        @NotNull(message = "관리자에게 문의해주세요.")
        UUID uid,

        @Schema(description = "출결 소유 권한", example = "USER")
        @NotNull(message = "관리자에게 문의해주세요.")
        AttendanceAuth attendanceAuth
) {
}
