package com.check.user_check.config.security.handler;

import com.check.user_check.entity.RefreshTokenRecord;
import com.check.user_check.service.response.basic.RefreshTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final RefreshTokenService refreshTokenService;

    @Override
    public void onLogoutSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication
    ) throws IOException, ServletException {

        Cookie[] cookies = request.getCookies();

        String refreshToken = Arrays.stream(cookies)
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        RefreshTokenRecord findByToken = refreshTokenService.findByTokenValue(refreshToken);
        if(findByToken != null){
            findByToken.expireToken();
            refreshTokenService.save(findByToken);
        }

        Cookie cookie = new Cookie("refreshToken", null);

        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
