package com.check.user_check.service.response.admin;

import com.check.user_check.config.security.CustomUserDetails;
import com.check.user_check.dto.ResultResponse;
import com.check.user_check.dto.response.admin.AttendanceTargetResponse;
import com.check.user_check.entity.Attendance;
import com.check.user_check.entity.User;
import com.check.user_check.service.response.basic.AttendanceService;
import com.check.user_check.service.response.basic.AttendanceTargetService;
import com.check.user_check.service.response.basic.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminTargetResponseService {

    private final AttendanceTargetService attendanceTargetService;

    private final AttendanceService attendanceService;
    private final UserService userService;

    private void ownerCheck(UUID uid, Attendance attendance){
        User user = userService.findById(uid);

        attendanceTargetService.ownerCheck(user, attendance);
    }

    private void userCheck(UUID uid, Attendance attendance){
        User user = userService.findById(uid);
        attendanceTargetService.userCheck(user, attendance);
    }

    public ResponseEntity<List<AttendanceTargetResponse>> readAttendanceTarget(
            UUID attendanceId,
            CustomUserDetails customUserDetails
    ){
        UUID uid = customUserDetails.getUserUuid();
        Attendance attendance = attendanceService.findById(attendanceId);

        userCheck(uid, attendance);

        List<AttendanceTargetResponse> result = attendanceTargetService.findTargetUserAllByTargetId(attendance);

        return ResponseEntity.ok(
                result
        );
    }
}
