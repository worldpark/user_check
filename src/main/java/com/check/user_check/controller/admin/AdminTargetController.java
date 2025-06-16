package com.check.user_check.controller.admin;

import com.check.user_check.config.security.CustomUserDetails;
import com.check.user_check.config.swagger.annotation.ResultCreatedListResponse;
import com.check.user_check.config.swagger.annotation.ResultUpdateAndDeleteResponse;
import com.check.user_check.dto.ResultResponse;
import com.check.user_check.dto.request.attendance.target.AttendanceTargetRequest;
import com.check.user_check.dto.response.admin.AttendanceTargetResponse;
import com.check.user_check.service.response.admin.AdminTargetResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@Tag(name = "AttendanceTarget", description = "출석 대상 정보 어드민 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/attendance-target")
public class AdminTargetController {

    private final AdminTargetResponseService adminTargetResponseService;

    @Operation(summary = "출결 대상자 목록")
    @GetMapping
    public ResponseEntity<List<AttendanceTargetResponse>> readAttendanceTarget(
            @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return adminTargetResponseService.readAttendanceTarget(customUserDetails);
    }

    @Operation(summary = "출결 대상자 생성")
    @PostMapping
    @ResultCreatedListResponse
    public ResponseEntity<ResultResponse<List<UUID>>> createAttendanceTarget(
            @RequestBody @Valid AttendanceTargetRequest targetListCreateRequests,
            @AuthenticationPrincipal CustomUserDetails customUserDetails){

        return adminTargetResponseService.createAttendanceTarget(targetListCreateRequests, customUserDetails);
    }

    @Operation(summary = "출결 대상 삭제")
    @DeleteMapping("/{attendanceTargetId}")
    @ResultUpdateAndDeleteResponse
    public ResponseEntity<ResultResponse<Void>> deleteAttendanceTarget(
            @PathVariable UUID attendanceTargetId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return adminTargetResponseService.deleteAttendanceTarget(attendanceTargetId, customUserDetails);
    }
}
