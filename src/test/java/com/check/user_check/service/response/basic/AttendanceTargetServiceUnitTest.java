package com.check.user_check.service.response.basic;

import com.check.user_check.entity.AttendanceTarget;
import com.check.user_check.entity.User;
import com.check.user_check.enumeratedType.Role;
import com.check.user_check.exception.custom.DataIntegrityViolationWithCodeException;
import com.check.user_check.exception.custom.EntityNotFoundWithCodeException;
import com.check.user_check.repository.AttendanceTargetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttendanceTargetServiceUnitTest {

    @Mock
    private AttendanceTargetRepository attendanceTargetRepository;

    @InjectMocks
    private AttendanceTargetService attendanceTargetService;

    @Test
    void findByIdThrowsWhenMissing() {
        UUID targetId = UUID.randomUUID();
        when(attendanceTargetRepository.findById(targetId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> attendanceTargetService.findById(targetId))
                .isInstanceOf(EntityNotFoundWithCodeException.class);
    }

    @Test
    void saveAllReturnsTargetIds() {
        AttendanceTarget first = target(UUID.randomUUID());
        AttendanceTarget second = target(UUID.randomUUID());
        when(attendanceTargetRepository.saveAll(List.of(first, second))).thenReturn(List.of(first, second));

        assertThat(attendanceTargetService.saveAll(List.of(first, second)))
                .containsExactly(first.getTargetId(), second.getTargetId());
    }

    @Test
    void saveAllWrapsDataIntegrityViolation() {
        AttendanceTarget target = target(UUID.randomUUID());
        when(attendanceTargetRepository.saveAll(List.of(target)))
                .thenThrow(new DataIntegrityViolationException("duplicate"));

        assertThatThrownBy(() -> attendanceTargetService.saveAll(List.of(target)))
                .isInstanceOf(DataIntegrityViolationWithCodeException.class)
                .extracting("code")
                .isEqualTo("040302");
    }

    @Test
    void findAllFetchDelegates() {
        List<AttendanceTarget> targets = List.of(target(UUID.randomUUID()));
        when(attendanceTargetRepository.findAllFetch()).thenReturn(targets);

        assertThat(attendanceTargetService.findAllFetch()).isSameAs(targets);
    }

    @Test
    void deleteDelegates() {
        AttendanceTarget target = target(UUID.randomUUID());

        attendanceTargetService.delete(target);

        verify(attendanceTargetRepository).delete(target);
    }

    private AttendanceTarget target(UUID id) {
        User admin = new User(UUID.randomUUID(), "admin", "password", "Admin", Role.ROLE_ADMIN);
        User user = new User(UUID.randomUUID(), "user", "password", "User", Role.ROLE_USER);
        return new AttendanceTarget(id, admin, user);
    }
}
