package com.check.user_check.service.response.admin;

import com.check.user_check.config.security.CustomUserDetails;
import com.check.user_check.dto.ResultResponse;
import com.check.user_check.dto.request.attendance.target.AttendanceTargetRequest;
import com.check.user_check.dto.response.admin.AttendanceTargetResponse;
import com.check.user_check.entity.Attendance;
import com.check.user_check.entity.AttendanceSetting;
import com.check.user_check.entity.AttendanceTarget;
import com.check.user_check.entity.User;
import com.check.user_check.enumeratedType.AttendanceStatus;
import com.check.user_check.service.response.basic.AttendanceService;
import com.check.user_check.service.response.basic.AttendanceSettingService;
import com.check.user_check.service.response.basic.AttendanceTargetService;
import com.check.user_check.util.LocalDateTimeCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminTargetResponseService {

    private final AttendanceTargetService attendanceTargetService;

    private final AttendanceService attendanceService;

    private final AttendanceSettingService attendanceSettingService;

    public ResponseEntity<List<AttendanceTargetResponse>> readAttendanceTarget(
            CustomUserDetails customUserDetails){

        List<AttendanceTarget> findTargets = attendanceTargetService.findAllFetch();

        List<AttendanceTargetResponse> result = findTargets.stream()
                .map(attendanceTarget -> AttendanceTargetResponse.builder()
                        .targetId(attendanceTarget.getTargetId())
                        .createAt(attendanceTarget.getCreatedAt())
                        .username(attendanceTarget.getUser().getUsername())
                        .name(attendanceTarget.getUser().getName())
                        .build())
                .collect(Collectors.toList());


        return ResponseEntity.ok(
                result
        );
    }

    @Transactional(readOnly = false)
    public ResponseEntity<ResultResponse<List<UUID>>> createAttendanceTarget(
            AttendanceTargetRequest attendanceTargetRequest,
            CustomUserDetails customUserDetails) {

        List<AttendanceTarget> attendanceTargets = attendanceTargetRequest.targetRequests().stream()
                        .map(targetRequest -> AttendanceTarget.builder()
                                .assignedUser(new User(customUserDetails.getUserId()))
                                .user(new User(targetRequest.userId()))
                                .build())
                        .collect(Collectors.toList());

        AttendanceSetting attendanceSetting = attendanceSettingService.findAttendanceSetting();
        LocalDateTime assignDateTime =
                LocalDateTimeCreator.getNowLocalDateTimeToLocalTime(attendanceSetting.getAttendanceTime());

        List<Attendance> attendances = attendanceTargetRequest.targetRequests().stream()
                .map(targetRequest -> Attendance.builder()
                        .attendanceDate(assignDateTime)
                        .status(AttendanceStatus.ABSENT)
                        .user(new User(targetRequest.userId()))
                        .build())
                .collect(Collectors.toList());

        attendanceService.saveAll(attendances);

        List<UUID> results = attendanceTargetService.saveAll(attendanceTargets);

        return ResultResponse.created(results);
    }

    @Transactional(readOnly = false)
    public ResponseEntity<ResultResponse<Void>> deleteAttendanceTarget(
            UUID attendanceTargetId, CustomUserDetails customUserDetails) {

        AttendanceTarget findTarget = attendanceTargetService.findById(attendanceTargetId);

        AttendanceSetting attendanceSetting = attendanceSettingService.findAttendanceSetting();
        LocalDateTime assignDateTime =
                LocalDateTimeCreator.getNowLocalDateTimeToLocalTime(attendanceSetting.getAttendanceTime());

        Attendance findAttendance = attendanceService.findByUserIdAndAttendanceDate(
                findTarget.getUser().getUserId(), assignDateTime);

        if(findAttendance != null){
            attendanceService.delete(findAttendance);
        }

        attendanceTargetService.delete(findTarget);

        return ResultResponse.deleteContent();
    }
}
