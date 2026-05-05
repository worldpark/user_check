package com.check.user_check.entity;

import com.check.user_check.enumeratedType.AttendanceStatus;
import com.check.user_check.enumeratedType.Role;
import com.check.user_check.repository.AttendanceRepository;
import com.check.user_check.repository.UserRepository;
import com.check.user_check.util.UUIDv6Generator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class AttendanceOptimisticLockTest {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void staleAttendanceUpdateFailsWithOptimisticLock() {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

        LocalDateTime attendanceDate = LocalDateTime.of(2026, 5, 5, 9, 0);

        UUID attendanceId = transactionTemplate.execute(status -> {
            User user = userRepository.save(new User(
                    UUIDv6Generator.generate(),
                    "user",
                    "password",
                    "user",
                    Role.ROLE_USER
            ));

            Attendance attendance = new Attendance(
                    UUIDv6Generator.generate(),
                    attendanceDate,
                    null,
                    AttendanceStatus.ABSENT,
                    "",
                    user
            );

            return attendanceRepository.saveAndFlush(attendance).getAttendanceId();
        });

        Attendance staleAttendance = transactionTemplate.execute(status -> {
            Attendance attendance = attendanceRepository.findById(attendanceId).orElseThrow();
            assertThat(attendance.getVersion()).isZero();
            return attendance;
        });

        transactionTemplate.executeWithoutResult(status -> {
            Attendance latestAttendance = attendanceRepository.findById(attendanceId).orElseThrow();
            latestAttendance.changeStatus(AttendanceStatus.PRESENT);
            latestAttendance.changeCheckTime(attendanceDate);
            attendanceRepository.flush();
            assertThat(latestAttendance.getVersion()).isEqualTo(1L);
        });

        staleAttendance.changeStatus(AttendanceStatus.LATE);
        staleAttendance.changeCheckTime(attendanceDate.plusMinutes(10));

        assertThatThrownBy(() -> transactionTemplate.executeWithoutResult(status -> {
            entityManager.merge(staleAttendance);
            entityManager.flush();
        })).isInstanceOfAny(
                OptimisticLockException.class,
                OptimisticLockingFailureException.class
        );
    }
}
