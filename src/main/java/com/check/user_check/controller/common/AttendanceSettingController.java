package com.check.user_check.controller.common;

import com.check.user_check.dto.response.admin.AttendanceSettingResponse;
import com.check.user_check.service.response.admin.AttendanceSettingResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "AttendanceSettingCommon", description = "출석 정보 공통 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/setting")
public class AttendanceSettingController {

    private final AttendanceSettingResponseService attendanceSettingResponseService;

    @Operation(summary = "출석 설정 정보 조회")
    @GetMapping
    public ResponseEntity<AttendanceSettingResponse> getAttendanceSetting(){
        return attendanceSettingResponseService.getAttendanceSetting();
    }
}
