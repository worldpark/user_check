package com.check.user_check.service.response.admin;

import com.check.user_check.config.security.CustomUserDetails;
import com.check.user_check.dto.ResultResponse;
import com.check.user_check.dto.request.AttendanceUpdateRequest;
import com.check.user_check.dto.response.common.AttendanceResponse;
import com.check.user_check.entity.Attendance;
import com.check.user_check.service.response.basic.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminAttendanceResponseService {

    private final AttendanceService attendanceService;

    public ResponseEntity<List<AttendanceResponse>> readAttendances(CustomUserDetails customUserDetails){

        List<Attendance> result = attendanceService.findAllFetch();

        return ResponseEntity.ok(
                result.stream()
                        .map(attendance -> AttendanceResponse.builder()
                                .attendanceId(attendance.getAttendanceId())
                                .attendanceDate(attendance.getAttendanceDate())
                                .checkTime(attendance.getCheckTime())
                                .status(attendance.getStatus())
                                .userId(attendance.getUser().getUserId())
                                .username(attendance.getUser().getUsername())
                                .build())
                        .collect(Collectors.toList())
        );
    }

    @Transactional(readOnly = false)
    public ResponseEntity<ResultResponse<Void>> updateAttendances(
            UUID attendanceId, AttendanceUpdateRequest updateRequest,
            CustomUserDetails customUserDetails){

        Attendance findAttendance = attendanceService.findById(attendanceId);
        findAttendance.changeStatus(updateRequest.status());
        findAttendance.changeCheckTime(updateRequest.checkTime());

        return ResultResponse.success();
    }
}
