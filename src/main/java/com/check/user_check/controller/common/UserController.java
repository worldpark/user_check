package com.check.user_check.controller.common;

import com.check.user_check.config.security.CustomUserDetails;
import com.check.user_check.config.swagger.annotation.ResultCreatedResponse;
import com.check.user_check.config.swagger.annotation.ResultUpdateAndDeleteResponse;
import com.check.user_check.dto.ResultResponse;
import com.check.user_check.dto.request.UserCreateRequest;
import com.check.user_check.dto.request.UserUpdateRequest;
import com.check.user_check.dto.response.user.UserResponse;
import com.check.user_check.service.response.common.UserResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@Tag(name = "UserCommon", description = "유저 정보 공통 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserResponseService userResponseService;

    @Operation(summary = "로그인 정보 조회")
    @GetMapping
    public ResponseEntity<UserResponse> readLoginInfo(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return userResponseService.readLoginInfo(customUserDetails);
    }

    @Deprecated
    @Operation(summary = "유저 정보 조회")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> readUser(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return userResponseService.readUser(id, customUserDetails);
    }

    @Deprecated
    @Operation(summary = "회원가입")
    @ResultCreatedResponse
    @PostMapping
    public ResponseEntity<ResultResponse<UUID>> createUser(
            @RequestBody @Valid UserCreateRequest userCreateRequest
    ){
        return userResponseService.createUser(userCreateRequest);
    }

    @Deprecated
    @Operation(summary = "유저 정보 수정")
    @PutMapping
    @ResultUpdateAndDeleteResponse
    public ResponseEntity<ResultResponse<Void>> updateUser(
            @RequestBody @Valid UserUpdateRequest userUpdateRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return userResponseService.updateUser(userUpdateRequest, customUserDetails);
    }

    @Deprecated
    @Operation(summary = "유저 회원 탈퇴")
    @DeleteMapping
    @ResultUpdateAndDeleteResponse
    public ResponseEntity<ResultResponse<Void>> deleteUser(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        return userResponseService.deleteUser(customUserDetails);
    }

}
