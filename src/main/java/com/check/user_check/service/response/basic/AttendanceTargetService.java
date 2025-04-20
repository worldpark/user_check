package com.check.user_check.service.response.basic;

import com.check.user_check.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttendanceTargetService {

    private final AttendanceRepository attendanceRepository;
}
