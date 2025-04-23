package com.check.user_check.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AttendanceCreateRequest(

        @NotNull(message = "출결 이름을 입력해주세요.")
        @Schema(description = "출결이름", example = "과학 시험")
        @Size(max = 20, min = 5, message = "출결 이릉은 5글자이상 20글자 이하로만 입력 가능합니다.")
        String attendanceName
) {}
