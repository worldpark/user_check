package com.check.user_check.service.response.admin;

import com.check.user_check.service.response.basic.AttendanceTargetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminTargetResponseService {

    private final AttendanceTargetService attendanceTargetService;
}
