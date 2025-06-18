package com.check.user_check.dto.response.admin;

import lombok.Builder;

import java.time.LocalTime;
import java.util.UUID;

@Builder
public record AttendanceSettingResponse(

        UUID infoId,
        Double latitude,
        Double longitude,
        LocalTime attendanceTime
) {
}
