package com.check.user_check.exception.token.refreshToken;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class RefreshTokenExceptionUnitTest {

    @Test
    void sendResponseErrorWritesStatusAndCode() throws Exception {
        RefreshTokenException exception = new RefreshTokenException(RefreshTokenError.BAD_SIGN);
        MockHttpServletResponse response = new MockHttpServletResponse();

        exception.sendResponseError(response);

        assertThat(response.getStatus()).isEqualTo(403);
        assertThat(response.getContentAsString()).contains("\"code\":\"010304\"");
    }
}
