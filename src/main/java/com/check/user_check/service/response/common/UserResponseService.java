package com.check.user_check.service.response.common;

import com.check.user_check.enumeratedType.Role;
import com.check.user_check.config.security.CustomUserDetails;
import com.check.user_check.dto.ResultResponse;
import com.check.user_check.dto.request.UserCreateRequest;
import com.check.user_check.dto.request.UserUpdateRequest;
import com.check.user_check.dto.response.UserListResponse;
import com.check.user_check.dto.response.user.UserResponse;
import com.check.user_check.entity.User;
import com.check.user_check.exception.custom.CustomException;
import com.check.user_check.service.response.basic.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserResponseService {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    private void authCheck(UUID id, CustomUserDetails customUserDetails){
        List<String> currentRoles = customUserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String currentRole = currentRoles.get(0);

        if(currentRole.equals(Role.ROLE_USER.getRoleName()) && !customUserDetails.getUserUuid().equals(id)){
            throw new CustomException(HttpStatus.UNAUTHORIZED, "000001", "허용되지 않은 요청");
        }
    }

    public ResponseEntity<UserListResponse> readAllUsers(){

        List<User> users = userService.findAll();

        return ResponseEntity.ok(
                UserListResponse.builder()
                        .users(users.stream().map(user -> UserResponse.builder()
                                        .uid(user.getUid())
                                        .userId(user.getUserId())
                                        .userName(user.getUserName())
                                        .role(user.getRole())
                                        .build())
                                .collect(Collectors.toList()))
                        .build()
        );
    }

    public ResponseEntity<UserResponse> readUser(UUID id, CustomUserDetails customUserDetails){

        authCheck(id, customUserDetails);
        User user = userService.findById(id);

        return ResponseEntity.ok(
                UserResponse.builder()
                        .uid(user.getUid())
                        .userId(user.getUserId())
                        .userName(user.getUserName())
                        .role(user.getRole())
                        .build()
        );
    }

    @Transactional
    public ResponseEntity<ResultResponse<UUID>> createUser(UserCreateRequest userCreateRequest){

        String password = passwordEncoder.encode(userCreateRequest.password());

        User user = User.builder()
                .userName(userCreateRequest.userName())
                .password(password)
                .userId(userCreateRequest.userId())
                .role(userCreateRequest.role())
                .build();

        UUID createdId = userService.save(user);

        return ResultResponse.created(createdId);
    }

    @Transactional
    public ResponseEntity<ResultResponse<Void>> updateUser(
            UserUpdateRequest userUpdateRequest
            , CustomUserDetails customUserDetails
    ){
        UUID uid = userUpdateRequest.uid();
        authCheck(uid, customUserDetails);

        String password = passwordEncoder.encode(userUpdateRequest.password());
        User user = User.builder()
                .uid(uid)
                .password(password)
                .userName(userUpdateRequest.userName())
                .role(userUpdateRequest.role())
                .build();

        userService.save(user);

        return ResultResponse.success();
    }

    @Transactional
    public ResponseEntity<ResultResponse<Void>> deleteUser(
            UUID id,
            CustomUserDetails customUserDetails){

        authCheck(id, customUserDetails);

        userService.delete(id);
        return ResultResponse.deleteContent();
    }
}
