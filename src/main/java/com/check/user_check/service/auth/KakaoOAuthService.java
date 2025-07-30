package com.check.user_check.service.auth;

import com.check.user_check.config.security.util.JWTUtil;
import com.check.user_check.dto.request.auto.KakaoLoginRequest;
import com.check.user_check.entity.RefreshTokenRecord;
import com.check.user_check.entity.User;
import com.check.user_check.enumeratedType.Role;
import com.check.user_check.repository.UserRepository;
import com.check.user_check.service.response.basic.RefreshTokenService;
import com.check.user_check.util.UUIDv6Generator;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoOAuthService {

    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    private final RestTemplate restTemplate;
    public final RefreshTokenService refreshTokenService;

    @Value("${access-token-expiration-time}")
    private int accessTokenExpirationTime;

    @Value("${refresh-token-expiration-time}")
    private int refreshTokenExpirationTime;

    public ResponseEntity<?> kakaoLogin(
            KakaoLoginRequest kakaoAccessToken
    ){

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(kakaoAccessToken.accessToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                entity,
                Map.class
        );

        Map<String, Object> kakaoAccount = (Map<String, Object>) response.getBody().get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        String kakaoId = Long.toString((Long) response.getBody().get("id"));
        String nickname = (String) profile.get("nickname");

        userRepository.findByUsername(kakaoId)
                .orElseGet(() -> userRepository.save(User.builder()
                        .userId(UUIDv6Generator.generate())
                        .username(kakaoId)
                        .password(null)
                        .name(nickname)
                        .role(Role.ROLE_USER)
                        .build()));

        Map<String, Object> claim = Map.of("sub", kakaoId);

        String accessToken = jwtUtil.generateToken(claim, accessTokenExpirationTime);
        String refreshToken = jwtUtil.generateToken(claim, refreshTokenExpirationTime);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpirationTime)
                .sameSite("Strict")
                .build();

        LocalDateTime refreshTokenExpireAt = LocalDateTime.now().plusSeconds(refreshTokenExpirationTime);
        RefreshTokenRecord newRefreshTokenRecord = RefreshTokenRecord.builder()
                .username(kakaoId)
                .tokenValue(refreshToken)
                .expireAt(refreshTokenExpireAt)
                .build();

        refreshTokenService.save(newRefreshTokenRecord);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("accessToken", accessToken));

    }
}
