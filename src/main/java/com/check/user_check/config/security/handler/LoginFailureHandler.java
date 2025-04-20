package com.check.user_check.config.security.handler;

import com.check.user_check.config.security.util.AuthenticationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;

public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request
            , HttpServletResponse response
            , AuthenticationException exception) throws IOException, ServletException {

        int status = HttpServletResponse.SC_BAD_REQUEST;
        String code = "0101";
        String msg = "";

        if (exception instanceof LockedException){
            code += "01";
            msg = "이미 탈퇴한 회원입니다.";
        } else if (exception instanceof DisabledException) {
            code += "02";
            status = HttpServletResponse.SC_FORBIDDEN;
            msg = "비활성화 된 계정 입니다.";
        } else if(exception instanceof CredentialsExpiredException) {
            code += "03";
            msg = "만료된 아이디 입니다.";
        } else if(exception instanceof BadCredentialsException) {
            code += "04";
            msg = "아이디 또는 비밀번호가 일치하지 않습니다.";
        } else if (exception instanceof InternalAuthenticationServiceException) {
            code += "05";
            status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            msg = "요청을 처리할 수 없습니다. 관리자에게 문의하세요. ";
        } else if (exception instanceof UsernameNotFoundException) {
            code += "06";
            msg = "아이디가 존재하지 않습니다.";
        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            code += "07";
            msg = "인증할 수 없는 정보입니다.";
        } else {
            code += "00";
            msg = "알 수 없는 오류로 로그인 요청을 처리할 수 없습니다. 관리자에게 문의하세요.";
        }

        AuthenticationUtil.sendResponseError(
                response,
                status,
                msg,
                code
        );

    }
}
