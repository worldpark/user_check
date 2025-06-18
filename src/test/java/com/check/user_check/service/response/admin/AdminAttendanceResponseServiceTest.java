package com.check.user_check.service.response.admin;

import com.check.user_check.dto.response.admin.AttendanceSummaryResponse;
import com.check.user_check.dto.response.common.AttendanceResponse;
import com.check.user_check.entity.Attendance;
import com.check.user_check.entity.User;
import com.check.user_check.enumeratedType.AttendanceStatus;
import com.check.user_check.enumeratedType.Role;
import com.check.user_check.repository.UserRepository;
import com.check.user_check.service.response.basic.AttendanceService;
import com.check.user_check.util.UUIDv6Generator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class AdminAttendanceResponseServiceTest {

    @Autowired
    private AdminAttendanceResponseService attendanceResponseService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final LocalDateTime localDateTime = LocalDateTime.now();

    @BeforeEach
    private void setup(){

        List<User> saveUsers = new ArrayList<>();
        List<Attendance> saveAttendances = new ArrayList<>();

        for(int i = 0; i < 10; i++){

            User saveUser = new User(
                    UUIDv6Generator.generate(),
                    "user" + (i + 1),
                    passwordEncoder.encode("1111"),
                    "userName" + (i + 1),
                    Role.ROLE_USER
            );
            saveUsers.add(saveUser);

            Attendance saveAttendance = null;
            if(i % 2 == 0){
                saveAttendance = new Attendance(
                        UUIDv6Generator.generate(),
                        localDateTime,
                        localDateTime,
                        AttendanceStatus.PRESENT,
                        "",
                        saveUser
                );
            }else{
                saveAttendance = new Attendance(
                        UUIDv6Generator.generate(),
                        localDateTime,
                        null,
                        AttendanceStatus.ABSENT,
                        "",
                        saveUser
                );
            }
            saveAttendances.add(saveAttendance);
        }

        userRepository.saveAll(saveUsers);
        attendanceService.saveAll(saveAttendances);
    }

    @Test
    void readAttendances() {

        Pageable pageable = PageRequest.of(0, 5);
        ResponseEntity<Page<AttendanceResponse>> pageResponseEntity =
                attendanceResponseService.readAttendances(localDateTime, pageable);

        assertThat(pageResponseEntity.getBody().getTotalPages()).isEqualTo(2);
        assertThat(pageResponseEntity.getBody().getTotalElements()).isEqualTo(10);
    }

    @Test
    void readAttendanceSummary() {

        ResponseEntity<AttendanceSummaryResponse> attendanceSummary =
                attendanceResponseService.readAttendanceSummary(localDateTime);

        assertThat(attendanceSummary.getBody().totalData().getValue()).isEqualTo(10);
        assertThat(attendanceSummary.getBody().absentData().getValue()).isEqualTo(5);
        assertThat(attendanceSummary.getBody().presentData().getValue()).isEqualTo(5);
    }
}