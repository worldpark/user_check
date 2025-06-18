package com.check.user_check.service.response.admin;

import com.check.user_check.dto.ResultResponse;
import com.check.user_check.dto.request.attendance.AttendanceUpdateRequest;
import com.check.user_check.dto.response.admin.AttendanceSummaryResponse;
import com.check.user_check.dto.response.common.AttendanceResponse;
import com.check.user_check.entity.Attendance;
import com.check.user_check.service.response.basic.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminAttendanceResponseService {

    private final AttendanceService attendanceService;

    public ResponseEntity<Page<AttendanceResponse>> readAttendances(LocalDateTime dateTime, Pageable pageable){

        LocalDateTime nowDateTime = LocalDateTime.now();

        LocalDateTime startOfDay = dateTime.toLocalDate().atStartOfDay();
        LocalDateTime startOfNextDay = dateTime.toLocalDate().plusDays(1).atStartOfDay();
        Page<Attendance> result = attendanceService.findAllByDateTime(startOfDay, startOfNextDay, pageable);

        LocalDate today = LocalDate.now();
        LocalDateTime minTime = today.atStartOfDay();

        Page<AttendanceResponse> resultDatas = result.map(attendance -> {

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
                checkTime = attendance.getCheckTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
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
        });

        return ResponseEntity.ok(
                resultDatas
        );
    }

    @Transactional(readOnly = false)
    public ResponseEntity<ResultResponse<Void>> updateAttendances(
            UUID attendanceId, AttendanceUpdateRequest updateRequest){

        Attendance findAttendance = attendanceService.findById(attendanceId);
        findAttendance.changeStatus(updateRequest.status());
        findAttendance.changeCheckTime(updateRequest.checkTime());

        return ResultResponse.success();
    }

    public ResponseEntity<AttendanceSummaryResponse> readAttendanceSummary(LocalDateTime dateTime) {

        LocalDateTime startOfDay = dateTime.toLocalDate().atStartOfDay();
        LocalDateTime startOfNextDay = dateTime.toLocalDate().plusDays(1).atStartOfDay();

        AttendanceSummaryResponse attendanceSummaryResponse = new AttendanceSummaryResponse(
                new AttendanceSummaryResponse.SummaryData("text-black", "전체 인원"),
                new AttendanceSummaryResponse.SummaryData("text-blue-500", "출석 인원"),
                new AttendanceSummaryResponse.SummaryData("text-red-500", "결석 인원"));

        Long countIsCheck = attendanceService.findCountIsCheck(startOfDay, startOfNextDay);
        Long countNoCheck = attendanceService.findCountNoCheck(startOfDay, startOfNextDay);

        attendanceSummaryResponse.presentData().setValue(countIsCheck);
        attendanceSummaryResponse.absentData().setValue(countNoCheck);
        attendanceSummaryResponse.totalData().setValue(countIsCheck + countNoCheck);

        return ResponseEntity.ok(
                attendanceSummaryResponse
        );
    }
}
