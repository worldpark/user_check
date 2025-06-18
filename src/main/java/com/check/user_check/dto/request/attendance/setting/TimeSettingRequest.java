package com.check.user_check.dto.request.attendance.setting;

import com.check.user_check.converter.deserializer.LenientLocalTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record TimeSettingRequest(

        @NotNull(message = "시간 데이터를 입력해주세요.")
        @Schema(description = "출석 시간", example = "09:00:00")
        @JsonDeserialize(using = LenientLocalTimeDeserializer.class)
        LocalTime attendanceTime
){
}
