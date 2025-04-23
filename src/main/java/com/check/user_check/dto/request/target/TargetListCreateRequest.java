package com.check.user_check.dto.request.target;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record TargetListCreateRequest(

        @Schema(description = "출결 ID", example = "d72be5b0-2031-11f0-8337-bd8c4a471c40")
        @NotNull(message = "관리자에게 문의해주세요.")
        UUID attendanceId,

        @Schema(description = "출결 생성 대상자 정보 리스트")
        List<TargetCreateRequest> targetList

) {}
