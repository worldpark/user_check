package com.check.user_check.service.response.basic;

import com.check.user_check.entity.Attendance;
import com.check.user_check.entity.User;
import com.check.user_check.enumeratedType.AttendanceStatus;
import com.check.user_check.enumeratedType.Role;
import com.check.user_check.exception.custom.DataIntegrityViolationWithCodeException;
import com.check.user_check.exception.custom.EntityNotFoundWithCodeException;
import com.check.user_check.repository.AttendanceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceUnitTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @InjectMocks
    private AttendanceService attendanceService;

    @Test
    void findByIdThrowsEntityNotFoundWhenMissing() {
        UUID attendanceId = UUID.randomUUID();
        when(attendanceRepository.findById(attendanceId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> attendanceService.findById(attendanceId))
                .isInstanceOf(EntityNotFoundWithCodeException.class);
    }

    @Test
    void saveReturnsAttendanceId() {
        Attendance attendance = attendance(UUID.randomUUID());
        when(attendanceRepository.save(attendance)).thenReturn(attendance);

        UUID result = attendanceService.save(attendance);

        assertThat(result).isEqualTo(attendance.getAttendanceId());
    }

    @Test
    void saveWrapsDataIntegrityViolation() {
        Attendance attendance = attendance(UUID.randomUUID());
        when(attendanceRepository.save(attendance)).thenThrow(new DataIntegrityViolationException("duplicate"));

        assertThatThrownBy(() -> attendanceService.save(attendance))
                .isInstanceOf(DataIntegrityViolationWithCodeException.class)
                .extracting("code")
                .isEqualTo("030302");
    }

    @Test
    void saveAllReturnsSavedIds() {
        Attendance first = attendance(UUID.randomUUID());
        Attendance second = attendance(UUID.randomUUID());
        when(attendanceRepository.saveAll(List.of(first, second))).thenReturn(List.of(first, second));

        List<UUID> result = attendanceService.saveAll(List.of(first, second));

        assertThat(result).containsExactly(first.getAttendanceId(), second.getAttendanceId());
    }

    @Test
    void findAllByDateTimeDelegatesToRepository() {
        LocalDateTime min = LocalDateTime.now().minusDays(1);
        LocalDateTime max = LocalDateTime.now();
        PageRequest pageable = PageRequest.of(0, 10);
        PageImpl<Attendance> page = new PageImpl<>(List.of(attendance(UUID.randomUUID())));
        when(attendanceRepository.findAllByDateTime(min, max, pageable)).thenReturn(page);

        assertThat(attendanceService.findAllByDateTime(min, max, pageable)).isSameAs(page);
    }

    @Test
    void findByUserIdAndAttendanceDateReturnsNullWhenMissing() {
        UUID userId = UUID.randomUUID();
        LocalDateTime attendanceDate = LocalDateTime.now();
        when(attendanceRepository.findByUserIdAndAttendanceDate(userId, attendanceDate)).thenReturn(Optional.empty());

        assertThat(attendanceService.findByUserIdAndAttendanceDate(userId, attendanceDate)).isNull();
    }

    @Test
    void updateAttendanceDateDelegatesToRepository() {
        LocalDateTime attendanceDate = LocalDateTime.now();
        LocalDateTime min = attendanceDate.minusHours(1);
        LocalDateTime max = attendanceDate.plusHours(1);

        attendanceService.updateAttendanceDate(attendanceDate, min, max);

        verify(attendanceRepository).updateAttendanceDate(attendanceDate, min, max);
    }

    @Test
    void countMethodsDelegateToRepository() {
        LocalDateTime min = LocalDateTime.now().minusDays(1);
        LocalDateTime max = LocalDateTime.now();
        when(attendanceRepository.findCountNoCheck(min, max)).thenReturn(3L);
        when(attendanceRepository.findCountIsCheck(min, max)).thenReturn(7L);

        assertThat(attendanceService.findCountNoCheck(min, max)).isEqualTo(3L);
        assertThat(attendanceService.findCountIsCheck(min, max)).isEqualTo(7L);
    }

    private Attendance attendance(UUID id) {
        return new Attendance(
                id,
                LocalDateTime.now(),
                null,
                AttendanceStatus.ABSENT,
                "",
                new User(UUID.randomUUID(), "user", "password", "User", Role.ROLE_USER)
        );
    }
}
