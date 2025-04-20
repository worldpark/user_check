package com.check.user_check.service.response.user;

import com.check.user_check.service.response.basic.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserAttendanceResponseService {

    private final AttendanceService attendanceService;
}
