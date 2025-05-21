package com.check.user_check.service.response.admin;

import com.check.user_check.config.security.CustomUserDetails;
import com.check.user_check.dto.ResultResponse;
import com.check.user_check.dto.request.target.TargetListCreateRequest;
import com.check.user_check.dto.response.admin.AttendanceTargetResponse;
import com.check.user_check.entity.Attendance;
import com.check.user_check.entity.AttendanceTarget;
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
import java.util.stream.Collectors;

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
            CustomUserDetails customUserDetails){
        UUID uid = customUserDetails.getUserUuid();
        Attendance attendance = attendanceService.findById(attendanceId);

        userCheck(uid, attendance);

        List<AttendanceTargetResponse> result = attendanceTargetService.findTargetUserAllByTargetId(attendance);

        return ResponseEntity.ok(
                result
        );
    }


    public ResponseEntity<ResultResponse<UUID>> createAttendanceTarget(
            TargetListCreateRequest targetListCreateRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails){

        Attendance attendance = attendanceService.findById(targetListCreateRequest.attendanceId());

        /*
        User 엔티티를 불러와야하는데 findById로 해당하는 유저쿼리 다 날리면 성능낭비임, 프록시 객체로 받으면?
        -> getReferenceById() 함수 지원해준다함 jpa2.2 기준, jpa가 관리하기 때문에 다른 필드를 사용할때 편리함(좋다는건 아님)

        그냥 User 엔티티에 Id만 넣어서 생성해도 괜찮음

        결론 : 성능을 원한다면 후자, 변경 가능성이 있다면 전자
         */


        List<AttendanceTarget> attendanceTargets = targetListCreateRequest.targetList().stream()
                .map(targetCreateRequest -> {
                    return AttendanceTarget.builder()
                            .build();
                })
                .collect(Collectors.toList());

        return null;
    }
}
