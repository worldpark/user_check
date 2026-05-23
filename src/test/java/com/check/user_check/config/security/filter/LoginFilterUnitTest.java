package com.check.user_check.config.security.filter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginFilterUnitTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    void postRequestAuthenticatesUsingJsonCredentials() {
        LoginFilter loginFilter = new LoginFilter("/api/user/auth/login");
        loginFilter.setAuthenticationManager(authenticationManager);

        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/user/auth/login");
        request.setContentType("application/json");
        request.setContent("""
                {"username":"tester","password":"Password123"}
                """.getBytes());
        MockHttpServletResponse response = new MockHttpServletResponse();
        UsernamePasswordAuthenticationToken authenticated =
                UsernamePasswordAuthenticationToken.authenticated(
                        "tester",
                        "Password123",
                        List.of()
                );
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authenticated);

        Authentication result = loginFilter.attemptAuthentication(request, response);

        assertThat(result).isSameAs(authenticated);
        verify(authenticationManager).authenticate(any(Authentication.class));
    }

    @Test
    void nonPostRequestReturnsMethodNotAllowedWithoutAuthentication() throws Exception {
        LoginFilter loginFilter = new LoginFilter("/api/user/auth/login");
        loginFilter.setAuthenticationManager(authenticationManager);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/user/auth/login");
        MockHttpServletResponse response = new MockHttpServletResponse();

        Authentication result = loginFilter.attemptAuthentication(request, response);

        assertThat(result).isNull();
        assertThat(response.getStatus()).isEqualTo(405);
        assertThat(response.getContentAsString()).contains("\"code\":\"010110\"");
    }

    @Test
    void missingPasswordThrowsIllegalStateException() {
        LoginFilter loginFilter = new LoginFilter("/api/user/auth/login");
        loginFilter.setAuthenticationManager(authenticationManager);

        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/user/auth/login");
        request.setContentType("application/json");
        request.setContent("""
                {"username":"tester"}
                """.getBytes());
        MockHttpServletResponse response = new MockHttpServletResponse();

        assertThatThrownBy(() -> loginFilter.attemptAuthentication(request, response))
                .isInstanceOf(IllegalStateException.class);
    }
}
