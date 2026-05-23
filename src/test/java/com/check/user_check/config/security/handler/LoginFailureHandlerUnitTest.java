package com.check.user_check.config.security.handler;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class LoginFailureHandlerUnitTest {

    private final LoginFailureHandler loginFailureHandler = new LoginFailureHandler();

    @Test
    void badCredentialsMapsToExpectedStatusAndCode() throws ServletException, IOException {
        MockHttpServletResponse response = new MockHttpServletResponse();

        loginFailureHandler.onAuthenticationFailure(
                new MockHttpServletRequest(),
                response,
                new BadCredentialsException("bad credentials")
        );

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getContentAsString()).contains("\"code\":\"010104\"");
    }

    @Test
    void disabledAccountMapsToForbidden() throws ServletException, IOException {
        MockHttpServletResponse response = new MockHttpServletResponse();

        loginFailureHandler.onAuthenticationFailure(
                new MockHttpServletRequest(),
                response,
                new DisabledException("disabled")
        );

        assertThat(response.getStatus()).isEqualTo(403);
        assertThat(response.getContentAsString()).contains("\"code\":\"010102\"");
    }

    @Test
    void usernameNotFoundMapsToExpectedCode() throws ServletException, IOException {
        MockHttpServletResponse response = new MockHttpServletResponse();

        loginFailureHandler.onAuthenticationFailure(
                new MockHttpServletRequest(),
                response,
                new UsernameNotFoundException("not found")
        );

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getContentAsString()).contains("\"code\":\"010106\"");
    }
}
