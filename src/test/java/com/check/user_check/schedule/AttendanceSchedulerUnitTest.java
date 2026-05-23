package com.check.user_check.schedule;

import com.check.user_check.dto.AttendanceSettingDto;
import com.check.user_check.entity.AttendanceTarget;
import com.check.user_check.entity.User;
import com.check.user_check.enumeratedType.Role;
import com.check.user_check.service.AttendanceSettingCacheService;
import com.check.user_check.service.response.basic.AttendanceTargetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttendanceSchedulerUnitTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private AttendanceTargetService attendanceTargetService;

    @Mock
    private AttendanceSettingCacheService attendanceSettingCacheService;

    @InjectMocks
    private AttendanceScheduler attendanceScheduler;

    @Test
    void createAttendanceBuildsBatchInsertUsingCachedAttendanceTime() throws Exception {
        LocalTime attendanceTime = LocalTime.of(8, 30);
        UUID firstUserId = UUID.randomUUID();
        UUID secondUserId = UUID.randomUUID();
        User firstUser = new User(firstUserId, "user1", "password", "User One", Role.ROLE_USER);
        User secondUser = new User(secondUserId, "user2", "password", "User Two", Role.ROLE_USER);
        List<AttendanceTarget> targets = List.of(
                new AttendanceTarget(UUID.randomUUID(), new User(UUID.randomUUID()), firstUser),
                new AttendanceTarget(UUID.randomUUID(), new User(UUID.randomUUID()), secondUser)
        );
        when(attendanceSettingCacheService.cachingAttendanceSetting()).thenReturn(
                AttendanceSettingDto.builder()
                        .attendanceTime(attendanceTime)
                        .build()
        );
        when(attendanceTargetService.findAllFetch()).thenReturn(targets);

        attendanceScheduler.createAttendance();

        ArgumentCaptor<BatchPreparedStatementSetter> setterCaptor =
                ArgumentCaptor.forClass(BatchPreparedStatementSetter.class);
        verify(jdbcTemplate).batchUpdate(eq("""
                    INSERT INTO attendance (attendance_id, attendance_date, status, user_id)
                    VALUES (?, ?, ?, ?)
                """), setterCaptor.capture());

        BatchPreparedStatementSetter setter = setterCaptor.getValue();
        PreparedStatement firstStatement = mock(PreparedStatement.class);
        PreparedStatement secondStatement = mock(PreparedStatement.class);
        LocalDateTime expectedDateTime = LocalDateTime.of(LocalDate.now(), attendanceTime);

        assertThat(setter.getBatchSize()).isEqualTo(2);

        setter.setValues(firstStatement, 0);
        verify(firstStatement).setTimestamp(2, Timestamp.valueOf(expectedDateTime));
        verify(firstStatement).setString(3, "ABSENT");
        verify(firstStatement).setString(4, firstUserId.toString());

        setter.setValues(secondStatement, 1);
        verify(secondStatement).setTimestamp(2, Timestamp.valueOf(expectedDateTime));
        verify(secondStatement).setString(3, "ABSENT");
        verify(secondStatement).setString(4, secondUserId.toString());
    }
}
