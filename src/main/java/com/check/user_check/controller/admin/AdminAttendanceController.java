package com.check.user_check.controller.admin;

import com.check.user_check.config.security.CustomUserDetails;
import com.check.user_check.config.swagger.annotation.ResultUpdateAndDeleteResponse;
import com.check.user_check.dto.ResultResponse;
import com.check.user_check.dto.request.AttendanceUpdateRequest;
import com.check.user_check.dto.response.common.AttendanceResponse;
import com.check.user_check.service.response.admin.AdminAttendanceResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Attendance", description = "출결 기록 DTO")
@RestController
@RequestMapping("/api/admin/attendance")
public class AdminAttendanceController {

    private final AdminAttendanceResponseService attendanceResponseService;

    public AdminAttendanceController(AdminAttendanceResponseService attendanceResponseService) {
        this.attendanceResponseService = attendanceResponseService;
    }

    @Operation(summary = "출결 기록 조회")
    @GetMapping
    public ResponseEntity<List<AttendanceResponse>> readAttendances(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
        ){
        return attendanceResponseService.readAttendances(customUserDetails);
    }

    @Operation(summary = "출결 기록 수정")
    @PutMapping("/{attendanceId}")
    @ResultUpdateAndDeleteResponse
    public ResponseEntity<ResultResponse<Void>> updateAttendances(
            @PathVariable UUID attendanceId,
            @RequestBody @Valid AttendanceUpdateRequest attendanceUpdateRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return attendanceResponseService.updateAttendances(attendanceId, attendanceUpdateRequest, customUserDetails);
    }

}
