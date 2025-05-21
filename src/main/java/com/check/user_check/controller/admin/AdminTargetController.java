package com.check.user_check.controller.admin;

import com.check.user_check.config.security.CustomUserDetails;
import com.check.user_check.dto.ResultResponse;
import com.check.user_check.dto.request.target.TargetListCreateRequest;
import com.check.user_check.dto.response.admin.AttendanceTargetResponse;
import com.check.user_check.service.response.admin.AdminTargetResponseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/{attendanceId}/attendancetarget")
public class AdminTargetController {

    private final AdminTargetResponseService adminTargetResponseService;

    @GetMapping
    public ResponseEntity<List<AttendanceTargetResponse>> readAttendanceTarget(
            @PathVariable UUID attendanceId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return adminTargetResponseService.readAttendanceTarget(attendanceId, customUserDetails);
    }

    @PostMapping
    public ResponseEntity<ResultResponse<UUID>> createAttendanceTarget(
            @Valid TargetListCreateRequest targetListCreateRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails){

        return null;
    }
}
