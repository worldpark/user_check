package com.check.user_check.config.security.handler;

import com.check.user_check.config.security.util.AuthenticationUtil;
import com.check.user_check.config.security.util.JWTUtil;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request
            , HttpServletResponse response
            , Authentication authentication) throws IOException, ServletException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> claim = Map.of("sub", authentication.getName());

        String accessToken = jwtUtil.generateToken(claim, AuthenticationUtil.accessTokenExpirationTime);
        String refreshToken = jwtUtil.generateToken(claim, AuthenticationUtil.accessTokenExpirationTime * 4);

        Map<String, String> tokens = Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );

        Map<String, Object> result = Map.of(
                "message", "로그인 되었습니다.",
                "data", tokens
        );

        Gson gson = new Gson();
        String json = gson.toJson(result);
        response.getWriter().println(json);
    }
}
