package com.check.user_check.controller.admin;

import com.check.user_check.config.swagger.annotation.ResultUpdateAndDeleteResponse;
import com.check.user_check.dto.ResultResponse;
import com.check.user_check.dto.request.attendance.AttendanceUpdateRequest;
import com.check.user_check.dto.response.admin.AttendanceSummaryResponse;
import com.check.user_check.dto.response.common.AttendanceResponse;
import com.check.user_check.service.response.admin.AdminAttendanceResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Tag(name = "Attendance", description = "출결 기록 어드민 API")
@RestController
@RequestMapping("/api/admin/attendance")
public class AdminAttendanceController {

    private final AdminAttendanceResponseService attendanceResponseService;

    public AdminAttendanceController(AdminAttendanceResponseService attendanceResponseService) {
        this.attendanceResponseService = attendanceResponseService;
    }

    @Operation(summary = "출결 기록 조회")
    @GetMapping
    public ResponseEntity<Page<AttendanceResponse>> readAttendances(
            @RequestParam("timestamp") LocalDateTime dateTime,
            @PageableDefault(size = 5) Pageable pageable
        ){
        return attendanceResponseService.readAttendances(dateTime, pageable);
    }

    @Operation(summary = "출석 통계 조회")
    @GetMapping("/summary")
    public ResponseEntity<AttendanceSummaryResponse> readAttendanceSummary(
            @RequestParam("timestamp") LocalDateTime dateTime){

        return attendanceResponseService.readAttendanceSummary(dateTime);
    }


    @Deprecated
    @Operation(summary = "출결 기록 수정")
    @PutMapping("/{attendanceId}")
    @ResultUpdateAndDeleteResponse
    public ResponseEntity<ResultResponse<Void>> updateAttendances(
            @PathVariable UUID attendanceId,
            @RequestBody @Valid AttendanceUpdateRequest attendanceUpdateRequest
    ){
        return attendanceResponseService.updateAttendances(attendanceId, attendanceUpdateRequest);
    }

}
