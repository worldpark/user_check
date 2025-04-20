package com.check.user_check.controller.user;

import com.check.user_check.service.response.user.UserAttendanceResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserAttendanceController {

    private final UserAttendanceResponseService attendanceResponseService;
}
