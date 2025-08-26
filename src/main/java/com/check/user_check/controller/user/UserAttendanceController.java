package com.check.user_check.controller.user;

import com.check.user_check.config.security.CustomUserDetails;
import com.check.user_check.config.swagger.annotation.ResultUpdateAndDeleteResponse;
import com.check.user_check.dto.ResultResponse;
import com.check.user_check.dto.SocketMessage;
import com.check.user_check.dto.response.common.ListAttendanceResponse;
import com.check.user_check.service.response.user.UserAttendanceResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@Tag(name = "AttendanceUser", description = "출결 기록 유저 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/attendance")
public class UserAttendanceController {

    private final UserAttendanceResponseService attendanceResponseService;
    private final SimpMessagingTemplate messagingTemplate;

    @Operation(summary = "출결 정보 조회")
    @GetMapping
    public ResponseEntity<ListAttendanceResponse> readUserAttendanceList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return attendanceResponseService.readUserAttendanceList(customUserDetails);
    }

    @Operation(summary = "출결 정보 수정")
    @PutMapping("/{attendanceId}")
    @ResultUpdateAndDeleteResponse
    public ResponseEntity<ResultResponse<Void>> checkAttendance(
            @PathVariable UUID attendanceId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            HttpServletRequest httpServletRequest
    ){
        messagingTemplate.convertAndSend("/topic/public",
                new SocketMessage("System", "Data refresh"));
        return attendanceResponseService.checkAttendance(attendanceId, customUserDetails, httpServletRequest);
    }
}
