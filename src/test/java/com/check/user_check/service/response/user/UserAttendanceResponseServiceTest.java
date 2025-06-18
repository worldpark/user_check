package com.check.user_check.service.response.user;

import com.check.user_check.config.security.CustomUserDetails;
import com.check.user_check.dto.ResultResponse;
import com.check.user_check.dto.response.common.AttendanceResponse;
import com.check.user_check.dto.response.common.ListAttendanceResponse;
import com.check.user_check.entity.Attendance;
import com.check.user_check.entity.User;
import com.check.user_check.enumeratedType.AttendanceStatus;
import com.check.user_check.enumeratedType.Role;
import com.check.user_check.repository.UserRepository;
import com.check.user_check.service.response.basic.AttendanceService;
import com.check.user_check.util.UUIDv6Generator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UserAttendanceResponseServiceTest {

    @Autowired
    private UserAttendanceResponseService userAttendanceResponseService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final LocalDateTime localDateTime = LocalDateTime.now();
    private User user;
    private Attendance attendance;

    @BeforeEach
    private void setup(){

        User saveUser = new User(
                UUIDv6Generator.generate(),
                "user",
                passwordEncoder.encode("1111"),
                "userName",
                Role.ROLE_USER
        );
        this.user = saveUser;

        Attendance saveAttendance = new Attendance(
                UUIDv6Generator.generate(),
                localDateTime,
                null,
                AttendanceStatus.ABSENT,
                "",
                saveUser
        );
        this.attendance = saveAttendance;

        userRepository.save(saveUser);
        attendanceService.save(saveAttendance);
    }

    @Test
    void checkAttendance() {

        CustomUserDetails customUserDetails = new CustomUserDetails(
                user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                List.of(
                        new SimpleGrantedAuthority("ROLE_USER")
                )
        );
        ResponseEntity<ResultResponse<Void>> response =
                userAttendanceResponseService.checkAttendance(attendance.getAttendanceId(), customUserDetails);

        Attendance findAttendance = attendanceService.findById(attendance.getAttendanceId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(findAttendance.getCheckTime()).isNotNull();
        assertThat(findAttendance.getStatus()).isEqualTo(AttendanceStatus.LATE);
    }

    @Test
    void readUserAttendanceList() {

        CustomUserDetails customUserDetails = new CustomUserDetails(
                user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                List.of(
                        new SimpleGrantedAuthority("ROLE_USER")
                )
        );

        ResponseEntity<ListAttendanceResponse> listAttendance =
                userAttendanceResponseService.readUserAttendanceList(customUserDetails);

        List<AttendanceResponse> attendanceResponses = listAttendance.getBody().attendanceResponses();

        assertThat(attendanceResponses.size()).isEqualTo(1);
    }
}