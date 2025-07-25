package com.check.user_check.config.security.handler;


import com.check.user_check.config.security.util.AuthenticationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request
            , HttpServletResponse response
            , AccessDeniedException accessDeniedException) throws IOException, ServletException {
        AuthenticationUtil.sendResponseError(
                response,
                HttpServletResponse.SC_FORBIDDEN,
                "접근 권한이 없습니다.",
                "010108"
        );
    }
}
