package com.check.user_check.controller.admin;

import com.check.user_check.config.security.CustomUserDetails;
import com.check.user_check.config.swagger.annotation.ResultUpdateAndDeleteResponse;
import com.check.user_check.dto.ResultResponse;
import com.check.user_check.dto.request.attendance.setting.PositionSettingRequest;
import com.check.user_check.dto.request.attendance.setting.TimeSettingRequest;
import com.check.user_check.dto.response.admin.AttendanceSettingResponse;
import com.check.user_check.service.response.admin.AttendanceSettingResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "AttendanceSetting", description = "출석 정보 어드민 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/setting")
public class AdminAttendanceSettingController {

    private final AttendanceSettingResponseService attendanceSettingResponseService;

    @Deprecated
    @Operation(summary = "출석 설정 정보 조회")
    @GetMapping
    public ResponseEntity<AttendanceSettingResponse> getAttendanceSetting(){
        return attendanceSettingResponseService.getAttendanceSetting();
    }

    @Operation(summary = "출석 시간 수정")
    @ResultUpdateAndDeleteResponse
    @PutMapping("/time")
    public ResponseEntity<ResultResponse<Void>> updateAttendanceSetting(
            @RequestBody @Valid TimeSettingRequest timeSettingRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return attendanceSettingResponseService.updateTimeSetting(timeSettingRequest, customUserDetails);
    }

    @Operation(summary = "출석 좌표 수정")
    @ResultUpdateAndDeleteResponse
    @PutMapping("/position")
    public ResponseEntity<ResultResponse<Void>> updateAttendanceSetting(
            @RequestBody @Valid PositionSettingRequest positionSettingRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return attendanceSettingResponseService.updatePositionSetting(positionSettingRequest, customUserDetails);
    }
}
