package com.check.user_check.service.response.admin;

import com.check.user_check.config.security.CustomUserDetails;
import com.check.user_check.dto.ResultResponse;
import com.check.user_check.dto.request.target.AttendanceTargetRequest;
import com.check.user_check.dto.request.target.TargetListCreateRequest;
import com.check.user_check.dto.response.admin.AttendanceTargetResponse;
import com.check.user_check.entity.Attendance;
import com.check.user_check.entity.AttendanceTarget;
import com.check.user_check.entity.User;
import com.check.user_check.enumeratedType.AttendanceStatus;
import com.check.user_check.service.response.basic.AttendanceService;
import com.check.user_check.service.response.basic.AttendanceTargetService;
import com.check.user_check.service.response.basic.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminTargetResponseService {

    private final AttendanceTargetService attendanceTargetService;

    private final AttendanceService attendanceService;

    public ResponseEntity<List<AttendanceTargetResponse>> readAttendanceTarget(
            CustomUserDetails customUserDetails){

        List<AttendanceTarget> findTargets = attendanceTargetService.findAllFetch();

        List<AttendanceTargetResponse> result = findTargets.stream()
                .map(attendanceTarget -> AttendanceTargetResponse.builder()
                        .targetId(attendanceTarget.getTargetId())
                        .attendanceDate(attendanceTarget.getAttendanceDate())
                        .createAt(attendanceTarget.getCreatedAt())
                        .assignedUsername(attendanceTarget.getAssignedUser().getUsername())
                        .username(attendanceTarget.getUser().getUsername())
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
                                .attendanceDate(targetRequest.attendanceDate())
                                .assignedUser(new User(customUserDetails.getUserId()))
                                .user(new User(targetRequest.userId()))
                                .build())
                        .collect(Collectors.toList());

        List<Attendance> attendances = attendanceTargetRequest.targetRequests().stream()
                .map(targetRequest -> Attendance.builder()
                        .attendanceDate(targetRequest.attendanceDate())
                        .status(AttendanceStatus.ABSENT)
                        .user(new User(targetRequest.userId()))
                        .build())
                .collect(Collectors.toList());

        attendanceService.saveAll(attendances);

        List<UUID> results = attendanceTargetService.saveAll(attendanceTargets);

        return ResultResponse.created(results);
    }

    @Transactional(readOnly = false)
    public ResponseEntity<ResultResponse<Void>> updateAttendanceTarget(
            UUID attendanceTargetId,
            AttendanceTargetRequest.TargetRequest targetRequest,
            CustomUserDetails customUserDetails) {

        AttendanceTarget findTarget = attendanceTargetService.findById(attendanceTargetId);

        Attendance findAttendance = attendanceService.findByUserIdAndAttendanceDate(
                targetRequest.userId(), findTarget.getAttendanceDate());

        findTarget.changeAttendanceDate(targetRequest.attendanceDate());
        findAttendance.changeAttendanceDate(targetRequest.attendanceDate());


        return ResultResponse.success();
    }

    @Transactional(readOnly = false)
    public ResponseEntity<ResultResponse<Void>> deleteAttendanceTarget(
            UUID attendanceTargetId, CustomUserDetails customUserDetails) {

        AttendanceTarget findTarget = attendanceTargetService.findById(attendanceTargetId);
        attendanceTargetService.delete(findTarget);

        Attendance findAttendance = attendanceService.findByUserIdAndAttendanceDate(
                findTarget.getUser().getUserId(), findTarget.getAttendanceDate());

        attendanceService.delete(findAttendance);

        return ResultResponse.deleteContent();
    }
}
