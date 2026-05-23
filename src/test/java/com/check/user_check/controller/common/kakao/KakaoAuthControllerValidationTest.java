package com.check.user_check.controller.common.kakao;

import com.check.user_check.dto.request.auto.KakaoLoginRequest;
import com.check.user_check.service.auth.KakaoOAuthService;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class KakaoAuthControllerValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void kakaoLoginRejectsBlankAccessToken() throws Exception {
        KakaoAuthController controller = new KakaoAuthController(mock(KakaoOAuthService.class));
        Method method = KakaoAuthController.class.getMethod("kakaoLogin", KakaoLoginRequest.class);

        Set<?> violations = validator.forExecutables()
                .validateParameters(controller, method, new Object[]{new KakaoLoginRequest(" ")});

        assertThat(violations).isNotEmpty();
    }
}
