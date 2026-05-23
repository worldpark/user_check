package com.check.user_check.controller.common.kakao;

import com.check.user_check.dto.request.auto.KakaoLoginRequest;
import com.check.user_check.service.auth.KakaoOAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/auth/kakao")
public class KakaoAuthController {

    private final KakaoOAuthService kakaoOAuthService;

    @PostMapping
    public ResponseEntity<?> kakaoLogin(
            @RequestBody @Valid KakaoLoginRequest kakaoLoginRequest
    ){
        return kakaoOAuthService.kakaoLogin(kakaoLoginRequest);
    }
}
