package com.check.user_check.dto.request.auto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record KakaoLoginRequest(
        @NotBlank(message = "카카오 access token 을 입력해주세요.")
        @Size(max = 4096, message = "카카오 access token 길이가 너무 깁니다.")
        String accessToken
) {
}
