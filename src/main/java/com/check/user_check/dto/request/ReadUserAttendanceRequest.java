package com.check.user_check.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ReadUserAttendanceRequest(

        @NotNull(message = "올바르지 않은 계정입니다.")
        @Schema(description = "계정 ID", example = "cf9c2760-36c5-11f0-9062-c5dcca459193")
        UUID username


) {
}
