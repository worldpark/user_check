package com.check.user_check.service.response.common;

import com.check.user_check.dto.response.UserListResponse;
import com.check.user_check.dto.response.user.UserResponse;
import com.check.user_check.entity.Attendance;
import com.check.user_check.entity.AttendanceTarget;
import com.check.user_check.entity.User;
import com.check.user_check.enumeratedType.AttendanceStatus;
import com.check.user_check.enumeratedType.Role;
import com.check.user_check.repository.UserRepository;
import com.check.user_check.service.response.basic.AttendanceService;
import com.check.user_check.service.response.basic.AttendanceTargetService;
import com.check.user_check.util.UUIDv6Generator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserResponseServiceTest {

    @Autowired
    private UserResponseService userResponseService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttendanceTargetService attendanceTargetService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    private void setup(){

        List<User> saveUsers = new ArrayList<>();
        List<AttendanceTarget> saveAttendanceTargets = new ArrayList<>();

        User adminUser = new User(
                UUIDv6Generator.generate(),
                "test",
                passwordEncoder.encode("1111"),
                "testName",
                Role.ROLE_ADMIN
        );
        userRepository.save(adminUser);

        for(int i = 0; i < 10; i++){

            User saveUser = new User(
                    UUIDv6Generator.generate(),
                    "user" + (i + 1),
                    passwordEncoder.encode("1111"),
                    "userName" + (i + 1),
                    Role.ROLE_USER
            );
            saveUsers.add(saveUser);

            AttendanceTarget saveAttendanceTarget = null;

            if(i % 2 == 0){
                saveAttendanceTarget = new AttendanceTarget(
                        UUIDv6Generator.generate(),
                        adminUser,
                        saveUser
                );
                saveAttendanceTargets.add(saveAttendanceTarget);
            }
        }

        userRepository.saveAll(saveUsers);
        attendanceTargetService.saveAll(saveAttendanceTargets);
    }

    @Test
    void readAllUsers() {
        ResponseEntity<UserListResponse> userList = userResponseService.readAllUsers();

        List<UserResponse> users = userList.getBody().users();
        assertThat(users.size()).isEqualTo(11);
    }

    @Test
    void getNoTargetUsers() {
        ResponseEntity<UserListResponse> noTargetUsers = userResponseService.getNoTargetUsers();

        List<UserResponse> users = noTargetUsers.getBody().users();
        assertThat(users.size()).isEqualTo(5);
    }
}