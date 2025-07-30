package com.check.user_check.service.response.admin;

import com.check.user_check.config.security.CustomUserDetails;
import com.check.user_check.dto.ResultResponse;
import com.check.user_check.dto.request.attendance.setting.PositionSettingRequest;
import com.check.user_check.dto.request.attendance.setting.TimeSettingRequest;
import com.check.user_check.dto.AttendanceSettingDto;
import com.check.user_check.entity.AttendanceSetting;
import com.check.user_check.entity.User;
import com.check.user_check.service.response.basic.AttendanceService;
import com.check.user_check.service.response.basic.AttendanceSettingService;
import com.check.user_check.service.AttendanceSettingCacheService;
import com.check.user_check.util.LocalDateTimeCreator;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceSettingResponseService {

    private final AttendanceSettingService attendanceSettingService;
    private final AttendanceService attendanceService;

    private final EntityManager entityManager;
    private final AttendanceSettingCacheService attendanceSettingCacheService;

    public ResponseEntity<AttendanceSettingDto> getAttendanceSetting(){

        return ResponseEntity.ok(
                attendanceSettingCacheService.cachingAttendanceSetting()
        );
    }

    @Transactional(readOnly = false)
    public ResponseEntity<ResultResponse<Void>> updateTimeSetting(
            TimeSettingRequest timeSettingRequest,
            CustomUserDetails customUserDetails
    ) {
        User user = new User(customUserDetails.getUserId());

        AttendanceSetting attendanceSetting = attendanceSettingService.findAttendanceSetting();
        attendanceSetting.changeAttendanceTime(timeSettingRequest.attendanceTime(), user);
        entityManager.flush();
        entityManager.clear();

        attendanceSettingCacheService.cacheDataChange(attendanceSetting);

        LocalDate today = LocalDate.now();

        LocalDateTime assignDateTime =
                LocalDateTimeCreator.getNowLocalDateTimeToLocalTime(timeSettingRequest.attendanceTime());
        LocalDateTime todayTime = today.atStartOfDay();
        LocalDateTime tomorrowTime = today.plusDays(1).atStartOfDay();

        attendanceService.updateAttendanceDate(assignDateTime, todayTime, tomorrowTime);

        return ResultResponse.success();
    }

    @Transactional(readOnly = false)
    public ResponseEntity<ResultResponse<Void>> updatePositionSetting(
            PositionSettingRequest positionSettingRequest,
            CustomUserDetails customUserDetails
    ) {
        User user = new User(customUserDetails.getUserId());

        AttendanceSetting attendanceSetting = attendanceSettingService.findAttendanceSetting();
        attendanceSetting.changeAttendancePosition(
                positionSettingRequest.latitude(), positionSettingRequest.longitude(), user);

        attendanceSettingCacheService.cacheDataChange(attendanceSetting);

        return ResultResponse.success();
    }
}
