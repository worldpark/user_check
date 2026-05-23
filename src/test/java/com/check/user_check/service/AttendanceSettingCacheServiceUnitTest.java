package com.check.user_check.service;

import com.check.user_check.dto.AttendanceSettingDto;
import com.check.user_check.entity.AttendanceSetting;
import com.check.user_check.entity.User;
import com.check.user_check.enumeratedType.Role;
import com.check.user_check.service.response.basic.AttendanceSettingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttendanceSettingCacheServiceUnitTest {

    @Mock
    private AttendanceSettingService attendanceSettingService;

    @InjectMocks
    private AttendanceSettingCacheService attendanceSettingCacheService;

    @Test
    void cachingAttendanceSettingMapsEntityToDto() {
        AttendanceSetting setting = setting();
        when(attendanceSettingService.findAttendanceSetting()).thenReturn(setting);

        AttendanceSettingDto result = attendanceSettingCacheService.cachingAttendanceSetting();

        assertThat(result.infoId()).isEqualTo(setting.getInfoId());
        assertThat(result.latitude()).isEqualTo(setting.getLatitude());
        assertThat(result.longitude()).isEqualTo(setting.getLongitude());
        assertThat(result.attendanceTime()).isEqualTo(setting.getAttendanceTime());
    }

    @Test
    void cacheDataChangeMapsUpdatedEntityToDto() {
        AttendanceSetting setting = setting();

        AttendanceSettingDto result = attendanceSettingCacheService.cacheDataChange(setting);

        assertThat(result.infoId()).isEqualTo(setting.getInfoId());
        assertThat(result.latitude()).isEqualTo(setting.getLatitude());
        assertThat(result.longitude()).isEqualTo(setting.getLongitude());
        assertThat(result.attendanceTime()).isEqualTo(setting.getAttendanceTime());
    }

    private AttendanceSetting setting() {
        return new AttendanceSetting(
                UUID.randomUUID(),
                37.0,
                127.0,
                LocalTime.of(9, 0),
                new User(UUID.randomUUID(), "admin", "password", "Admin", Role.ROLE_ADMIN)
        );
    }
}
