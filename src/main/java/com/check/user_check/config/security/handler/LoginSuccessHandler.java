package com.check.user_check.config.security.handler;

import com.check.user_check.config.security.util.AuthenticationUtil;
import com.check.user_check.config.security.util.JWTUtil;
import com.check.user_check.entity.RefreshTokenRecord;
import com.check.user_check.service.response.basic.RefreshTokenService;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    private final int accessTokenExpirationTime;
    private final int refreshTokenExpirationTime;

    private final RefreshTokenService refreshTokenService;

    @Transactional
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request
            , HttpServletResponse response
            , Authentication authentication) throws IOException, ServletException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String username = authentication.getName();
        Map<String, Object> claim = Map.of("sub", username);

        String accessToken = jwtUtil.generateToken(claim, accessTokenExpirationTime);
        String refreshToken = jwtUtil.generateToken(claim, refreshTokenExpirationTime);

        Map<String, String> tokens = Map.of(
                "accessToken", accessToken
        );

        Map<String, Object> result = Map.of(
                "message", "로그인 되었습니다.",
                "data", tokens
        );

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpirationTime)
                .sameSite("Strict")
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        LocalDateTime refreshTokenExpireAt = LocalDateTime.now().plusSeconds(refreshTokenExpirationTime);

        RefreshTokenRecord refreshTokenRecord = RefreshTokenRecord.builder()
                .username(username)
                .tokenValue(refreshToken)
                .expireAt(refreshTokenExpireAt)
                .build();

        refreshTokenService.save(refreshTokenRecord);

        Gson gson = new Gson();
        String json = gson.toJson(result);
        response.getWriter().println(json);
    }
}
