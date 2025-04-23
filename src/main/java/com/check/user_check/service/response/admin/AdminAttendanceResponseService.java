package com.check.user_check.service.response.admin;

import com.check.user_check.config.security.CustomUserDetails;
import com.check.user_check.dto.ResultResponse;
import com.check.user_check.dto.request.AttendanceCreateRequest;
import com.check.user_check.dto.request.AttendanceUpdateRequest;
import com.check.user_check.dto.response.admin.AttendanceResponse;
import com.check.user_check.entity.Attendance;
import com.check.user_check.entity.AttendanceTarget;
import com.check.user_check.entity.User;
import com.check.user_check.service.response.basic.AttendanceService;
import com.check.user_check.service.response.basic.AttendanceTargetService;
import com.check.user_check.service.response.basic.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminAttendanceResponseService {

    private final AttendanceService attendanceService;
    private final AttendanceTargetService attendanceTargetService;
    private final UserService userService;

    private boolean authCheck(UUID attendanceId, List<Attendance> attendances){

        for(Attendance attendance : attendances){
            if(attendanceId.equals(attendance.getAttendanceId())){
                return true;
            }
        }
        throw new RuntimeException("권한이 없는 출결입니다.");
    }

    public ResponseEntity<List<AttendanceResponse>> readAttendances(CustomUserDetails customUserDetails){

        UUID userId = customUserDetails.getUserUuid();
        User user = userService.findById(userId);
        List<Attendance> attendances = attendanceService.findAllByUser(user);

        return ResponseEntity.ok(
                attendances.stream()
                        .map(attendance -> AttendanceResponse.builder()
                                .attendanceId(attendance.getAttendanceId())
                                .attendanceName(attendance.getAttendanceName())
                                .build())
                        .collect(Collectors.toList())
        );
    }

    @Transactional
    public ResponseEntity<ResultResponse<UUID>> createAttendance(AttendanceCreateRequest attendanceCreateRequest,
                                                                 CustomUserDetails customUserDetails){

        UUID userId = customUserDetails.getUserUuid();
        User user = userService.findById(userId);

        Attendance attendance = Attendance.builder()
                .attendanceName(attendanceCreateRequest.attendanceName())
                .build();

        Attendance result = attendanceService.save(attendance);

        AttendanceTarget attendanceTarget = AttendanceTarget.builder()
                .attendance(result)
                .user(user)
                .build();

        attendanceTargetService.save(attendanceTarget);

        return ResultResponse.created(result.getAttendanceId());
    }

    @Transactional
    public ResponseEntity<ResultResponse<Void>> updateAttendances(AttendanceUpdateRequest updateRequest,
                                                                  CustomUserDetails customUserDetails){

        UUID userId = customUserDetails.getUserUuid();
        User user = userService.findById(userId);

        List<Attendance> attendances = attendanceService.findAllByUser(user);
        authCheck(updateRequest.attendanceId(), attendances);

        Attendance attendance = Attendance.builder()
                .attendanceId(updateRequest.attendanceId())
                .attendanceName(updateRequest.attendanceName())
                .build();

        attendanceService.save(attendance);

        return ResultResponse.success();
    }

    @Transactional
    public ResponseEntity<ResultResponse<Void>> deleteAttendance(UUID attendanceId,
                                                                 CustomUserDetails customUserDetails){

        UUID userId = customUserDetails.getUserUuid();
        User user = userService.findById(userId);
        List<Attendance> attendances = attendanceService.findAllByUser(user);

        authCheck(attendanceId, attendances);

        Attendance attendance = attendanceService.findById(attendanceId);

        attendanceTargetService.deleteAllByAttendance(attendance);
        attendanceService.delete(attendance);


        return ResultResponse.deleteContent();
    }
}
