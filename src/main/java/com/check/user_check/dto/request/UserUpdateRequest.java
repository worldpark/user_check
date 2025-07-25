package com.check.user_check.dto.request;

import com.check.user_check.enumeratedType.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record UserUpdateRequest(

        @Schema(description = "유저 암호", example = "password")
        @NotNull(message = "암호를 입력해 주세요.")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{10,}$",
                message = "10자 이상, 영문 대소문자와 숫자를 모두 포함해야 합니다."
        )
        String password,

        @Schema(description = "유저 이름", example = "name")
        @NotNull(message = "이름을 입력해 주세요.")
        String name,

        @Schema(description = "유저 권한", example = "ROLE_USER")
        @NotNull(message = "관리자에게 문의해주세요.")
        Role role
) {}
