package com.check.user_check.service;

import com.check.user_check.dto.AttendanceSettingDto;
import com.check.user_check.entity.AttendanceSetting;
import com.check.user_check.service.response.basic.AttendanceSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AttendanceSettingCacheService {


    private final AttendanceSettingService attendanceSettingService;

    @Cacheable(value = "attendanceSetting", key = "'attendanceSetting'")
    public AttendanceSettingDto cachingAttendanceSetting(){

        AttendanceSetting attendanceSetting = attendanceSettingService.findAttendanceSetting();

        return AttendanceSettingDto.builder()
                .infoId(attendanceSetting.getInfoId())
                .latitude(attendanceSetting.getLatitude())
                .longitude(attendanceSetting.getLongitude())
                .attendanceTime(attendanceSetting.getAttendanceTime())
                .build();
    }

    @CachePut(value = "attendanceSetting", key = "'attendanceSetting'")
    public AttendanceSettingDto cacheDataChange(AttendanceSetting attendanceSetting){

        return AttendanceSettingDto.builder()
                .infoId(attendanceSetting.getInfoId())
                .latitude(attendanceSetting.getLatitude())
                .longitude(attendanceSetting.getLongitude())
                .attendanceTime(attendanceSetting.getAttendanceTime())
                .build();
    }
}
