package com.check.user_check.service.response.user;

import com.check.user_check.config.security.CustomUserDetails;
import com.check.user_check.dto.ResultResponse;
import com.check.user_check.dto.response.common.AttendanceResponse;
import com.check.user_check.dto.response.common.ListAttendanceResponse;
import com.check.user_check.entity.Attendance;
import com.check.user_check.enumeratedType.AttendanceStatus;
import com.check.user_check.exception.code.ClientExceptionCode;
import com.check.user_check.exception.code.ServerExceptionCode;
import com.check.user_check.exception.custom.CustomException;
import com.check.user_check.service.response.basic.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserAttendanceResponseService {

    private final AttendanceService attendanceService;

    private Attendance attendanceCheck(UUID attendanceId, CustomUserDetails customUserDetails){
        Attendance findAttendance = attendanceService.findFetchByAttendanceId(attendanceId);

        if(!findAttendance.getUser().getUserId().equals(customUserDetails.getUserId())){
            throw new CustomException(ClientExceptionCode.BAD_REQUEST);
        }

        return findAttendance;
    }

    public ResponseEntity<ResultResponse<Void>> checkAttendance(
            UUID attendanceId, CustomUserDetails customUserDetails
    ) {

        Attendance findAttendance = attendanceCheck(attendanceId, customUserDetails);
        LocalDateTime now = LocalDateTime.now();

        if(now.isBefore(findAttendance.getAttendanceDate())){
            findAttendance.changeStatus(AttendanceStatus.PRESENT);
        }else{
            findAttendance.changeStatus(AttendanceStatus.LATE);
        }

        findAttendance.changeCheckTime(now);

        attendanceService.save(findAttendance);

        return ResultResponse.success();
    }

    public ResponseEntity<ListAttendanceResponse> readUserAttendanceList(
            CustomUserDetails customUserDetails
    ) {
        UUID userId = customUserDetails.getUserId();
        
        LocalDateTime nowDateTime = LocalDateTime.now();

        LocalDate now = LocalDate.now();
        LocalDateTime minTime = now.atStartOfDay();
        LocalDateTime maxTime = now.atTime(LocalTime.MAX);

        List<Attendance> userAttendance = attendanceService.findUserAttendance(userId, minTime, maxTime);

        int resultSize = userAttendance.size();
        if(resultSize != 1 ){

            throw new CustomException(ServerExceptionCode.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(
                ListAttendanceResponse.builder()
                        .attendanceResponses(
                                userAttendance.stream()
                                        .map(attendance -> {

                                            String readStatus = attendance.readAttendanceStatus();
                                            LocalDateTime attendanceDate = attendance.getAttendanceDate();

                                            if(readStatus.equals("결석") && minTime.isBefore(attendanceDate)){
                                                if(nowDateTime.isAfter(attendanceDate)){
                                                    readStatus = "지각";
                                                }else{
                                                    readStatus = "미출석";
                                                }
                                            }

                                            String checkTime = "";

                                            if(attendance.getCheckTime() != null){
                                                checkTime = attendance.getCheckTime()
                                                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                                            }

                                            return AttendanceResponse.builder()
                                                    .attendanceId(attendance.getAttendanceId())
                                                    .attendanceDate(attendanceDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                                                    .checkTime(checkTime)
                                                    .status(readStatus)
                                                    .userId(attendance.getUser().getUserId())
                                                    .username(attendance.getUser().getUsername())
                                                    .name(attendance.getUser().getName())
                                                    .build();
                                        })
                                        .collect(Collectors.toList())
                        )
                        .build()
        );
    }
}
