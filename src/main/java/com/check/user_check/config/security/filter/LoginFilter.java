package com.check.user_check.config.security.filter;

import com.check.user_check.config.security.util.AuthenticationUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.util.Map;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    public LoginFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request
            , HttpServletResponse response) throws AuthenticationException {


        if(!request.getMethod().equalsIgnoreCase("POST")
            || request.getMethod().equalsIgnoreCase("OPTIONS")
        ){
            AuthenticationUtil.sendResponseError(
                    response,
                    HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                    "해당 메소드는 지원하지 않습니다.",
                    "010110"
            );
        }

        Map<String, String> jsonData = AuthenticationUtil.parseRequestJSON(request);

        if(jsonData.get("userId") == null
                || jsonData.get("userId").isEmpty()
                || jsonData.get("password") == null
                || jsonData.get("password").isEmpty()
        ){
            AuthenticationUtil.sendResponseError(
                    response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    "아이디 혹은 비밀번호가 누락되었습니다.",
                    "010111"
            );
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        jsonData.get("userId"),
                        jsonData.get("password")
                );

        return getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
    }
}
