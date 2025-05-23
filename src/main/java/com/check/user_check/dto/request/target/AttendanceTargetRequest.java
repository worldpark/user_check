package com.check.user_check.dto.request.target;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
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
                UUID userId,

                @NotNull(message = "날짜 정보가 정확하지 않습니다.")
                @Schema(description = "예정 시간", example = "2025-05-05 08:00:00")
                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                LocalDateTime attendanceDate
        ){}
}
