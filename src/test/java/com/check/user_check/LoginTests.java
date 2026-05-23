package com.check.user_check;

import com.check.user_check.entity.User;
import com.check.user_check.enumeratedType.TokenValid;
import com.check.user_check.enumeratedType.Role;
import com.check.user_check.repository.RefreshTokenRepository;
import com.check.user_check.repository.UserRepository;
import com.check.user_check.config.security.util.JWTUtil;
import com.check.user_check.util.UUIDv6Generator;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class LoginTests {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private JWTUtil jwtUtil;

    private boolean testSave = false;
    private String testId;
    private final String rawPassword = "1111";

    @BeforeAll
    private void setup(){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        testId = "test";
        Optional<User> user = userRepository.findByUsername(testId);

        if(user.isEmpty()){
            userRepository.save(User.builder()
                            .userId(UUIDv6Generator.generate())
                            .username(testId)
                            .name("test2")
                            .password(passwordEncoder.encode(rawPassword))
                            .role(Role.ROLE_USER)
                            .build());
            testSave = true;
        }
    }

    @Test
    void loginSuccessReturnsAccessTokenAndRefreshTokenCookie(){
        ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "username", testId,
                        "password", rawPassword
                ))
                .when()
                .post("/api/user/auth/login")
                .then()
                .statusCode(200)
                .extract();

        String accessToken = response.jsonPath().getString("data.accessToken");
        String refreshTokenCookie = response.cookie("refreshToken");

        assertThat(accessToken).isNotBlank();
        assertThat(jwtUtil.validateToken(accessToken)).isEqualTo(TokenValid.CORRECT);
        assertThat(jwtUtil.getTokenPayload(accessToken).get("sub")).isEqualTo(testId);
        assertThat(refreshTokenCookie).isNotBlank();
        assertThat(refreshTokenRepository.findByTokenValue(refreshTokenCookie)).isPresent();
        assertThat(response.header("Set-Cookie")).contains("HttpOnly");
    }

    @Test
    void loginFailsWhenPasswordIsWrong(){
        ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "username", testId,
                        "password", "wrong-password"
                ))
                .when()
                .post("/api/user/auth/login")
                .then()
                .statusCode(400)
                .extract();

        assertThat(response.jsonPath().getString("code")).isEqualTo("010104");
        assertThat(response.cookie("refreshToken")).isNull();
    }

    @AfterAll
    private void end(){

        if(testSave){
            Optional<User> user = userRepository.findByUsername(testId);
            userRepository.delete(user.orElseThrow(() -> new RuntimeException()));
        }
    }
}
