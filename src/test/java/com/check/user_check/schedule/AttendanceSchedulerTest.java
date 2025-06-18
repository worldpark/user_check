package com.check.user_check.schedule;


import com.check.user_check.service.response.basic.AttendanceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback(value = false)
class AttendanceSchedulerTest {

    @Autowired
    private AttendanceScheduler attendanceScheduler;

    @Autowired
    private AttendanceService attendanceService;

    @Test
    void insertAttendance(){
        attendanceScheduler.createAttendance();
    }
}