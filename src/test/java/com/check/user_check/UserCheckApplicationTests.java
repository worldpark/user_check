package com.check.user_check;

import com.check.user_check.config.security.util.JWTUtil;
import com.check.user_check.enumeratedType.TokenValid;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class UserCheckApplicationTests {

    private final JWTUtil jwtUtil = new JWTUtil("sOX6rwX65uDe+JphKfE9SAbC9oR3n61BN84t9zkN9QI=");

    @Test
    void jwtUtilGeneratesAndValidatesToken() {
        String token = jwtUtil.generateToken(Map.of("sub", "tester"), 10);

        assertThat(jwtUtil.validateToken(token)).isEqualTo(TokenValid.CORRECT);
        assertThat(jwtUtil.getTokenPayload(token).get("sub")).isEqualTo("tester");
    }

    @Test
    void jwtUtilReturnsExpiredForPastToken() {
        String token = jwtUtil.generateToken(Map.of("sub", "tester"), -1);

        assertThat(jwtUtil.validateToken(token)).isEqualTo(TokenValid.EXPIRE);
    }
}
