package com.check.user_check.controller.common;

import com.check.user_check.dto.response.UserListResponse;
import com.check.user_check.service.response.common.UserResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "UserList", description = "유저 리스트 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserListController {

    private final UserResponseService userResponseService;

    @GetMapping
    @Operation(summary = "유저 리스트 조회")
    public ResponseEntity<UserListResponse> getUsersInfo(){

        return userResponseService.readAllUsers();
    }

    @GetMapping("/no-target")
    @Operation(summary = "출석이 제외된 유저 조회")
    public ResponseEntity<UserListResponse> getNoTargetUsers(){
        return userResponseService.getNoTargetUsers();
    }
}
