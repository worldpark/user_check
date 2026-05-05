package com.check.user_check.entity;

import com.check.user_check.enumeratedType.Role;
import com.check.user_check.repository.AttendanceSettingRepository;
import com.check.user_check.repository.UserRepository;
import com.check.user_check.util.UUIDv6Generator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class AttendanceSettingOptimisticLockTest {

    @Autowired
    private AttendanceSettingRepository attendanceSettingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void staleAttendanceSettingUpdateFailsWithOptimisticLock() {
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

        UUID settingId = transactionTemplate.execute(status -> {
            User admin = userRepository.save(new User(
                    UUIDv6Generator.generate(),
                    "admin",
                    "password",
                    "admin",
                    Role.ROLE_ADMIN
            ));

            AttendanceSetting setting = new AttendanceSetting(
                    UUIDv6Generator.generate(),
                    10.,
                    10.,
                    LocalTime.of(9, 0),
                    admin
            );

            return attendanceSettingRepository.saveAndFlush(setting).getInfoId();
        });

        AttendanceSetting staleSetting = transactionTemplate.execute(status -> {
            AttendanceSetting setting = attendanceSettingRepository.findById(settingId).orElseThrow();
            assertThat(setting.getVersion()).isZero();
            return setting;
        });

        transactionTemplate.executeWithoutResult(status -> {
            AttendanceSetting latestSetting = attendanceSettingRepository.findById(settingId).orElseThrow();
            latestSetting.changeAttendanceTime(LocalTime.of(10, 0), latestSetting.getUser());
            attendanceSettingRepository.flush();
            assertThat(latestSetting.getVersion()).isEqualTo(1L);
        });

        staleSetting.changeAttendancePosition(11., 11., staleSetting.getUser());

        assertThatThrownBy(() -> transactionTemplate.executeWithoutResult(status -> {
            entityManager.merge(staleSetting);
            entityManager.flush();
        })).isInstanceOfAny(
                OptimisticLockException.class,
                OptimisticLockingFailureException.class
        );
    }
}
