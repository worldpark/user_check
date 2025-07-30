package com.check.user_check.dto;

import com.check.user_check.entity.AttendanceSetting;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.Builder;

import java.time.LocalTime;
import java.util.UUID;

@Builder
public record AttendanceSettingDto(

        UUID infoId,
        Double latitude,
        Double longitude,

        @JsonSerialize(using = LocalTimeSerializer.class)
        @JsonDeserialize(using = LocalTimeDeserializer.class)
        LocalTime attendanceTime
) {
}
