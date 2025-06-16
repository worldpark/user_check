package com.check.user_check.controller.common;

import com.check.user_check.config.security.util.JWTUtil;
import com.check.user_check.enumeratedType.TokenValid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;


@Tag(name = "JwtCheckController", description = "JWT 토큰 관련 API")
@RestController
@RequiredArgsConstructor
public class JwtCheckController {

    private final JWTUtil jwtUtil;

    @Operation(summary = "accessToken 유효성 검사")
    @GetMapping("/api/auth/validate-token")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String header) {
        String token = header.replace("Bearer ", "");
        TokenValid tokenValid = jwtUtil.validateToken(token);

        if(tokenValid == TokenValid.CORRECT){
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    @Operation(summary = "Refresh Token 검증 후 AccessToken 재발급")
    @PostMapping("/api/user/auth/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        String refreshToken = Arrays.stream(cookies)
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        TokenValid tokenValid = jwtUtil.validateToken(refreshToken);

        if (tokenValid == TokenValid.CORRECT) {
            return null;
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh Token expired");
        }
    }
}
