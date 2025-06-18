package com.check.user_check;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

//@SpringBootTest
//@ActiveProfiles("dev")
class UserCheckApplicationTests {

    @Test
    void contextLoads() {

        LocalDate now = LocalDate.now();

        LocalDateTime startTime = now.atStartOfDay();
        LocalDateTime endTime = now.atTime(LocalTime.MAX);

        System.out.println("now = " + startTime + ", " + endTime);
    }

}
