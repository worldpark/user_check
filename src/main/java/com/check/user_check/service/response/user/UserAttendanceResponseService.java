package com.check.user_check.service.response.user;

import com.check.user_check.config.security.CustomUserDetails;
import com.check.user_check.dto.ResultResponse;
import com.check.user_check.dto.request.AttendanceRequest;
import com.check.user_check.dto.response.common.AttendanceResponse;
import com.check.user_check.dto.response.common.ListAttendanceResponse;
import com.check.user_check.entity.Attendance;
import com.check.user_check.enumeratedType.AttendanceStatus;
import com.check.user_check.enumeratedType.Role;
import com.check.user_check.exception.custom.CustomException;
import com.check.user_check.service.response.basic.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserAttendanceResponseService {

    private final AttendanceService attendanceService;

    private void authCheck(UUID username, CustomUserDetails customUserDetails){

        if(!customUserDetails.getUserId().equals(username)){
            throw new CustomException(HttpStatus.UNAUTHORIZED, "000001", "허용되지 않은 요청");
        }
    }

    public ResponseEntity<ResultResponse<Void>> checkAttendance(
            UUID attendanceId, AttendanceRequest attendanceRequest, CustomUserDetails customUserDetails
    ) {
        authCheck(attendanceRequest.attendanceUserName(), customUserDetails);

        Attendance findAttendance = attendanceService.findById(attendanceId);
        LocalDateTime now = LocalDateTime.now();

        if(now.isBefore(findAttendance.getAttendanceDate())){
            findAttendance.changeStatus(AttendanceStatus.PRESENT);
        }

        findAttendance.changeCheckTime(now);

        return ResultResponse.success();
    }

    public ResponseEntity<ListAttendanceResponse> readUserAttendanceList(
            CustomUserDetails customUserDetails
    ) {
        UUID userId = customUserDetails.getUserId();

        LocalDate now = LocalDate.now();
        LocalDateTime minTime = now.atStartOfDay();
        LocalDateTime maxTime = now.atTime(LocalTime.MAX);

        List<Attendance> userAttendance = attendanceService.findUserAttendance(userId, minTime, maxTime);

        return ResponseEntity.ok(
                ListAttendanceResponse.builder()
                        .attendanceResponses(
                                userAttendance.stream()
                                        .map(attendance -> AttendanceResponse.builder()
                                                .attendanceId(attendance.getAttendanceId())
                                                .attendanceDate(attendance.getAttendanceDate())
                                                .checkTime(attendance.getCheckTime())
                                                .status(attendance.getStatus())
                                                .userId(attendance.getUser().getUserId())
                                                .username(attendance.getUser().getUsername())
                                                .build())
                                        .collect(Collectors.toList())
                        )
                        .build()
        );
    }
}
