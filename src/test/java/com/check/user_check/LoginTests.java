package com.check.user_check;

import com.check.user_check.entity.User;
import com.check.user_check.enumeratedType.Role;
import com.check.user_check.repository.UserRepository;
import com.check.user_check.util.UUIDv6Generator;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginTests {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private boolean testSave = false;
    private String testId;
    private String password;

    @BeforeAll
    private void setup(){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        Optional<User> user = userRepository.findByUserId(testId);
        testId = "testId2";
        password = passwordEncoder.encode("1111");

        if(user.isEmpty()){
            userRepository.save(User.builder()
                            .id(UUIDv6Generator.generate())
                            .userId(testId)
                            .userName("test2")
                            .password(password)
                            .role(Role.ROLE_USER)
                            .build());
            testSave = true;
        }
    }

    @Test
    void login(){
        System.out.println("aa");
    }

    @AfterAll
    private void end(){

        if(testSave){
            Optional<User> user = userRepository.findByUserId(testId);
            userRepository.delete(user.orElseThrow(() -> new RuntimeException()));
        }
    }
}
