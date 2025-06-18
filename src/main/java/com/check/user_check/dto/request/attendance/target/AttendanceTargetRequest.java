package com.check.user_check.dto.request.attendance.target;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record AttendanceTargetRequest(

        @Valid
        @NotNull(message = "대상자 정보가 없습니다.")
        List<TargetRequest> targetRequests
) {

        public record TargetRequest(
                @Schema(description = "대상자 고유 ID", example = "d72be5b0-2031-11f0-8337-bd8c4a471c40")
                @NotNull(message = "존재하지 않은 계정입니다.")
                UUID userId
        ){}
}
