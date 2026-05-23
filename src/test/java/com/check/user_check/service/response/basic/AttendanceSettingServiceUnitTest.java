package com.check.user_check.service.response.basic;

import com.check.user_check.entity.AttendanceSetting;
import com.check.user_check.entity.User;
import com.check.user_check.enumeratedType.Role;
import com.check.user_check.exception.custom.DataIntegrityViolationWithCodeException;
import com.check.user_check.exception.custom.EntityNotFoundWithCodeException;
import com.check.user_check.repository.AttendanceSettingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttendanceSettingServiceUnitTest {

    @Mock
    private AttendanceSettingRepository attendanceSettingRepository;

    @InjectMocks
    private AttendanceSettingService attendanceSettingService;

    @Test
    void findAttendanceSettingThrowsWhenNoSettingExists() {
        when(attendanceSettingRepository.findAll()).thenReturn(List.of());

        assertThatThrownBy(() -> attendanceSettingService.findAttendanceSetting())
                .isInstanceOf(EntityNotFoundWithCodeException.class)
                .extracting("code")
                .isEqualTo("050301");
    }

    @Test
    void findAttendanceSettingReturnsFirstSetting() {
        AttendanceSetting first = setting(UUID.randomUUID(), LocalTime.of(9, 0));
        AttendanceSetting second = setting(UUID.randomUUID(), LocalTime.of(10, 0));
        when(attendanceSettingRepository.findAll()).thenReturn(List.of(first, second));

        assertThat(attendanceSettingService.findAttendanceSetting()).isSameAs(first);
    }

    @Test
    void saveWrapsDataIntegrityViolation() {
        AttendanceSetting setting = setting(UUID.randomUUID(), LocalTime.of(9, 0));
        when(attendanceSettingRepository.save(setting)).thenThrow(new DataIntegrityViolationException("duplicate"));

        assertThatThrownBy(() -> attendanceSettingService.save(setting))
                .isInstanceOf(DataIntegrityViolationWithCodeException.class)
                .extracting("code")
                .isEqualTo("050302");
    }

    private AttendanceSetting setting(UUID id, LocalTime time) {
        return new AttendanceSetting(id, 37.0, 127.0, time, new User(UUID.randomUUID(), "admin", "pw", "Admin", Role.ROLE_ADMIN));
    }
}
