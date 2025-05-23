package com.check.user_check.controller.user;

import com.check.user_check.config.security.CustomUserDetails;
import com.check.user_check.config.swagger.annotation.ResultUpdateAndDeleteResponse;
import com.check.user_check.dto.ResultResponse;
import com.check.user_check.dto.request.AttendanceRequest;
import com.check.user_check.dto.response.common.ListAttendanceResponse;
import com.check.user_check.service.response.user.UserAttendanceResponseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/attendance")
public class UserAttendanceController {

    private final UserAttendanceResponseService attendanceResponseService;

    @GetMapping
    public ResponseEntity<ListAttendanceResponse> readUserAttendanceList(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return attendanceResponseService.readUserAttendanceList(customUserDetails);
    }

    @PutMapping("/{attendanceId}")
    @ResultUpdateAndDeleteResponse
    public ResponseEntity<ResultResponse<Void>> checkAttendance(
            @PathVariable UUID attendanceId,
            @Valid @RequestBody AttendanceRequest attendanceRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return attendanceResponseService.checkAttendance(attendanceId, attendanceRequest, customUserDetails);
    }
}
