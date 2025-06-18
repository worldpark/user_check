package com.check.user_check.util;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RequiredArgsConstructor
public class LocalDateTimeCreator {

    public static LocalDateTime getNowLocalDateTimeToLocalTime(LocalTime localTime){
//        ZoneId zoneId = ZoneId.of("Asia/Seoul");
        LocalDateTime assignDateTime = LocalDateTime.of(LocalDate.now(), localTime);

        return assignDateTime;
    }
}
