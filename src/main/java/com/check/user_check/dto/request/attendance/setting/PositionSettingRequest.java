package com.check.user_check.dto.request.attendance.setting;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record PositionSettingRequest(

        @NotNull(message = "위도를 입력해주세요.")
        @Schema(description = "위도", example = "37")
        Double latitude,

        @NotNull(message = "경도를 입력해주세요.")
        @Schema(description = "경도", example = "127")
        Double longitude
) {
}
