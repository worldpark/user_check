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

        if(jsonData.get("password") == null){
            throw new IllegalStateException("소셜 로그인 계정입니다. 직접 로그인이 불가능 합니다.");
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        jsonData.get("username"),
                        jsonData.get("password")
                );

        return getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
    }
}
