package com.check.user_check.service.response.admin;

import com.check.user_check.config.security.CustomUserDetails;
import com.check.user_check.dto.ResultResponse;
import com.check.user_check.dto.request.attendance.target.AttendanceTargetRequest;
import com.check.user_check.dto.response.admin.AttendanceTargetResponse;
import com.check.user_check.entity.Attendance;
import com.check.user_check.entity.AttendanceSetting;
import com.check.user_check.entity.AttendanceTarget;
import com.check.user_check.entity.User;
import com.check.user_check.enumeratedType.AttendanceStatus;
import com.check.user_check.enumeratedType.Role;
import com.check.user_check.exception.custom.EntityNotFoundWithCodeException;
import com.check.user_check.repository.AttendanceSettingRepository;
import com.check.user_check.repository.AttendanceTargetRepository;
import com.check.user_check.repository.UserRepository;
import com.check.user_check.service.response.basic.AttendanceService;
import com.check.user_check.service.response.basic.AttendanceTargetService;
import com.check.user_check.util.UUIDv6Generator;
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
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
class AdminTargetResponseServiceTest {

    @Autowired
    private AdminTargetResponseService adminTargetResponseService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private AttendanceTargetRepository attendanceTargetRepository;

    @Autowired
    private AttendanceTargetService attendanceTargetService;

    @Autowired
    private AttendanceSettingRepository attendanceSettingRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final LocalDateTime localDateTime = LocalDateTime.now();

    private User user;
    private User noTarget;
    private User adminUser;

    private Attendance attendance;
    private final UUID attendanceTargetId = UUIDv6Generator.generate();

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
        attendanceSettingRepository.save(attendanceSetting);
    }

    @Test
    void readAttendanceTarget() {

        ResponseEntity<List<AttendanceTargetResponse>> listResponseEntity =
                adminTargetResponseService.readAttendanceTarget();

        assertThat(listResponseEntity.getBody().size()).isEqualTo(1);

        AttendanceTargetResponse attendanceTargetResponse = listResponseEntity.getBody().get(0);
        AttendanceTarget findTarget = attendanceTargetService.findById(attendanceTargetId);

        assertThat(attendanceTargetResponse.targetId()).isEqualTo(findTarget.getTargetId());
        assertThat(attendanceTargetResponse.createAt()).isEqualTo(findTarget.getCreatedAt());
        assertThat(attendanceTargetResponse.username()).isEqualTo(this.user.getUsername());
        assertThat(attendanceTargetResponse.name()).isEqualTo(this.user.getName());
    }

    @Test
    void createAttendanceTarget() {

        CustomUserDetails customUserDetails = new CustomUserDetails(
                adminUser.getUserId(),
                adminUser.getUsername(),
                adminUser.getPassword(),
                List.of(
                        new SimpleGrantedAuthority("ROLE_USER")
                )
        );

        AttendanceTargetRequest attendanceTargetRequest = new AttendanceTargetRequest(
                List.of(
                        new AttendanceTargetRequest.TargetRequest(
                                noTarget.getUserId()
                        )
                )
        );

        ResponseEntity<ResultResponse<List<UUID>>> attendanceTarget =
                adminTargetResponseService.createAttendanceTarget(attendanceTargetRequest, customUserDetails);

        assertThat(attendanceTarget.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(attendanceTarget.getBody().getUuids().size()).isEqualTo(1);
    }

    @Test
    void deleteAttendanceTarget() {

        ResponseEntity<ResultResponse<Void>> response =
                adminTargetResponseService.deleteAttendanceTarget(attendanceTargetId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.RESET_CONTENT);

        assertThatThrownBy(() -> {attendanceTargetService.findById(attendanceTargetId);})
                .isInstanceOf(EntityNotFoundWithCodeException.class);
    }
}