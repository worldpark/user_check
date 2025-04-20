package com.check.user_check.config.security.handler;

import com.check.user_check.config.security.util.AuthenticationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request
            , HttpServletResponse response
            , AuthenticationException authException) throws IOException, ServletException {

        if(response.getStatus() == HttpServletResponse.SC_NOT_FOUND){
            AuthenticationUtil.sendResponseError(
                    response,
                    HttpServletResponse.SC_NOT_FOUND,
                    "페이지를 찾을수 없습니다.",
                    "010109"
            );
        }

    }
}
