package com.check.user_check.service.response.admin;

import com.check.user_check.config.security.CustomUserDetails;
import com.check.user_check.dto.ResultResponse;
import com.check.user_check.dto.request.attendance.setting.PositionSettingRequest;
import com.check.user_check.dto.request.attendance.setting.TimeSettingRequest;
import com.check.user_check.dto.AttendanceSettingDto;
import com.check.user_check.entity.Attendance;
import com.check.user_check.entity.AttendanceSetting;
import com.check.user_check.entity.AttendanceTarget;
import com.check.user_check.entity.User;
import com.check.user_check.enumeratedType.AttendanceStatus;
import com.check.user_check.enumeratedType.Role;
import com.check.user_check.repository.AttendanceSettingRepository;
import com.check.user_check.repository.AttendanceTargetRepository;
import com.check.user_check.repository.UserRepository;
import com.check.user_check.service.response.basic.AttendanceService;
import com.check.user_check.service.response.basic.AttendanceSettingService;
import com.check.user_check.util.UUIDv6Generator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class AttendanceSettingDtoServiceTest {

    @Autowired
    private AttendanceSettingResponseService attendanceSettingResponseService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private AttendanceTargetRepository attendanceTargetRepository;

    @Autowired
    private AttendanceSettingService attendanceSettingService;

    @Autowired
    private AttendanceSettingRepository attendanceSettingRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    private final LocalDateTime localDateTime = LocalDateTime.now();

    private User user;
    private User noTarget;
    private User adminUser;

    private Attendance attendance;
    private final UUID attendanceTargetId = UUIDv6Generator.generate();
    private AttendanceSetting attendanceSetting;

    @BeforeEach
    private void setup(){

        User saveAdmin = new User(
                UUIDv6Generator.generate(),
                "test",
                passwordEncoder.encode("1111"),
                "testName",
                Role.ROLE_ADMIN
        );
        this.adminUser = saveAdmin;
        userRepository.save(saveAdmin);

        User noTargetSaveUser = new User(
                UUIDv6Generator.generate(),
                "user2",
                passwordEncoder.encode("1111"),
                "userName2",
                Role.ROLE_USER
        );
        this.noTarget = noTargetSaveUser;
        userRepository.save(noTargetSaveUser);

        User saveUser = new User(
                UUIDv6Generator.generate(),
                "user",
                passwordEncoder.encode("1111"),
                "userName",
                Role.ROLE_USER
        );
        this.user = saveUser;
        userRepository.save(saveUser);

        Attendance saveAttendance = new Attendance(
                UUIDv6Generator.generate(),
                localDateTime,
                null,
                AttendanceStatus.ABSENT,
                "",
                saveUser
        );
        this.attendance = saveAttendance;
        attendanceService.save(saveAttendance);

        AttendanceTarget saveAttendanceTarget = new AttendanceTarget(
                attendanceTargetId,
                saveAdmin,
                saveUser
        );
        attendanceTargetRepository.save(saveAttendanceTarget);

        AttendanceSetting attendanceSetting = new AttendanceSetting(
                UUIDv6Generator.generate(),
                10.,
                10.,
                localDateTime.toLocalTime(),
                saveAdmin
        );
        this.attendanceSetting = attendanceSetting;
        attendanceSettingRepository.save(attendanceSetting);
    }

    @Test
    void getAttendanceSetting() {
        ResponseEntity<AttendanceSettingDto> response =
                attendanceSettingResponseService.getAttendanceSetting();

        AttendanceSettingDto attendanceSettingDto = response.getBody();

        assertThat(attendanceSetting.getInfoId()).isEqualTo(attendanceSettingDto.infoId());
        assertThat(attendanceSetting.getLatitude()).isEqualTo(attendanceSettingDto.latitude());
        assertThat(attendanceSetting.getLongitude()).isEqualTo(attendanceSettingDto.longitude());
        assertThat(attendanceSetting.getAttendanceTime()).isEqualTo(attendanceSettingDto.attendanceTime());
    }

    @Test
    void updateTimeSetting() {

        CustomUserDetails customUserDetails = new CustomUserDetails(
                adminUser.getUserId(),
                adminUser.getUsername(),
                adminUser.getPassword(),
                List.of(
                        new SimpleGrantedAuthority("ROLE_USER")
                )
        );

        LocalTime localTime = LocalTime.of(11, 0, 0);

        TimeSettingRequest timeSettingRequest = new TimeSettingRequest(
                localTime
        );

        ResponseEntity<ResultResponse<Void>> response =
                attendanceSettingResponseService.updateTimeSetting(timeSettingRequest, customUserDetails);

        AttendanceSetting attendanceSetting = attendanceSettingService.findAttendanceSetting();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(attendanceSetting.getAttendanceTime()).isEqualTo(localTime);
    }

    @Test
    void updatePositionSetting() {

        CustomUserDetails customUserDetails = new CustomUserDetails(
                adminUser.getUserId(),
                adminUser.getUsername(),
                adminUser.getPassword(),
                List.of(
                        new SimpleGrantedAuthority("ROLE_USER")
                )
        );

        PositionSettingRequest positionSettingRequest = new PositionSettingRequest(
                11.,
                11.
        );

        ResponseEntity<ResultResponse<Void>> response =
                attendanceSettingResponseService.updatePositionSetting(positionSettingRequest, customUserDetails);

        AttendanceSetting attendanceSetting = attendanceSettingService.findAttendanceSetting();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(attendanceSetting.getLatitude()).isEqualTo(11.);
        assertThat(attendanceSetting.getLongitude()).isEqualTo(11.);
    }
}