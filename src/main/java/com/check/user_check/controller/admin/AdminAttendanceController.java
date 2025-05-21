package com.check.user_check.controller.admin;

import com.check.user_check.config.security.CustomUserDetails;
import com.check.user_check.dto.ResultResponse;
import com.check.user_check.dto.request.AttendanceCreateRequest;
import com.check.user_check.dto.request.AttendanceUpdateRequest;
import com.check.user_check.dto.response.admin.AttendanceResponse;
import com.check.user_check.service.response.admin.AdminAttendanceResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Attendance", description = "출결 DTO")
@RestController
@RequestMapping("/api/admin/attendance")
public class AdminAttendanceController {

    private final AdminAttendanceResponseService attendanceResponseService;

    @Autowired
    public AdminAttendanceController(AdminAttendanceResponseService attendanceResponseService) {
        this.attendanceResponseService = attendanceResponseService;
    }

    @Operation(summary = "자신의 출결 목록")
    @GetMapping
    public ResponseEntity<List<AttendanceResponse>> readAttendances(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
        ){
        return attendanceResponseService.readAttendances(customUserDetails);
    }


    @Operation(summary = "출결 생성")
    @PostMapping
    public ResponseEntity<ResultResponse<UUID>> createAttendances(
            @RequestBody @Valid AttendanceCreateRequest attendanceCreateRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
            ){
        return attendanceResponseService.createAttendance(attendanceCreateRequest, customUserDetails);
    }

    @Operation(summary = "출결 수정")
    @PutMapping
    public ResponseEntity<ResultResponse<Void>> updateAttendances(
            @RequestBody @Valid AttendanceUpdateRequest attendanceUpdateRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return attendanceResponseService.updateAttendances(attendanceUpdateRequest, customUserDetails);
    }

    @Operation(summary = "출결 삭제")
    @DeleteMapping("/{attendanceId}")
    public ResponseEntity<ResultResponse<Void>> deleteAttendances(
            @RequestBody @Valid UUID attendanceId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return attendanceResponseService.deleteAttendance(attendanceId, customUserDetails);
    }

}
