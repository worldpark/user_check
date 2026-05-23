package com.check.user_check.service.response.common;

import com.check.user_check.config.security.CustomUserDetails;
import com.check.user_check.dto.ResultResponse;
import com.check.user_check.dto.request.UserCreateRequest;
import com.check.user_check.dto.request.UserUpdateRequest;
import com.check.user_check.dto.response.user.UserResponse;
import com.check.user_check.entity.User;
import com.check.user_check.enumeratedType.Role;
import com.check.user_check.exception.code.ClientExceptionCode;
import com.check.user_check.exception.custom.CustomException;
import com.check.user_check.service.response.basic.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserResponseServiceUnitTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserResponseService userResponseService;

    @Test
    void readUserRejectsDifferentUserForRoleUser() {
        UUID loginUserId = UUID.randomUUID();
        UUID targetUserId = UUID.randomUUID();
        CustomUserDetails customUserDetails = CustomUserDetails.builder()
                .userId(loginUserId)
                .userName("user")
                .password("encoded-password")
                .authorities(List.of(new SimpleGrantedAuthority(Role.ROLE_USER.getRoleName())))
                .build();

        assertThatThrownBy(() -> userResponseService.readUser(targetUserId, customUserDetails))
                .isInstanceOf(CustomException.class)
                .extracting("baseExceptionCode")
                .isEqualTo(ClientExceptionCode.BAD_REQUEST);

        verify(userService, never()).findById(any(UUID.class));
    }

    @Test
    void readUserAllowsAdminToReadAnotherUser() {
        UUID targetUserId = UUID.randomUUID();
        User user = new User(targetUserId, "target-user", "encoded-password", "Target", Role.ROLE_USER);
        CustomUserDetails customUserDetails = CustomUserDetails.builder()
                .userId(UUID.randomUUID())
                .userName("admin")
                .password("encoded-password")
                .authorities(List.of(new SimpleGrantedAuthority(Role.ROLE_ADMIN.getRoleName())))
                .build();
        when(userService.findById(targetUserId)).thenReturn(user);

        ResponseEntity<UserResponse> response = userResponseService.readUser(targetUserId, customUserDetails);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().userId()).isEqualTo(targetUserId);
        assertThat(response.getBody().username()).isEqualTo("target-user");
    }

    @Test
    void createUserEncodesPasswordAndDelegatesSave() {
        UUID createdId = UUID.randomUUID();
        UserCreateRequest request = new UserCreateRequest("tester1", "Password123", "Tester", Role.ROLE_USER);
        when(passwordEncoder.encode("Password123")).thenReturn("encoded-password");
        when(userService.save(any(User.class))).thenReturn(createdId);

        ResponseEntity<ResultResponse<UUID>> response = userResponseService.createUser(request);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(userService).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getUsername()).isEqualTo("tester1");
        assertThat(savedUser.getPassword()).isEqualTo("encoded-password");
        assertThat(savedUser.getName()).isEqualTo("Tester");
        assertThat(savedUser.getRole()).isEqualTo(Role.ROLE_USER);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getUuid()).isEqualTo(createdId);
    }

    @Test
    void updateUserChangesLoadedUserWithEncodedPassword() {
        UUID userId = UUID.randomUUID();
        UserUpdateRequest request = new UserUpdateRequest("Password123", "Changed", Role.ROLE_ADMIN);
        CustomUserDetails customUserDetails = CustomUserDetails.builder()
                .userId(userId)
                .userName("user")
                .password("encoded-password")
                .authorities(List.of(new SimpleGrantedAuthority(Role.ROLE_USER.getRoleName())))
                .build();
        User user = new User(userId, "user", "old-password", "Old", Role.ROLE_USER);
        when(passwordEncoder.encode("Password123")).thenReturn("new-encoded-password");
        when(userService.findById(userId)).thenReturn(user);

        ResponseEntity<ResultResponse<Void>> response = userResponseService.updateUser(request, customUserDetails);

        verify(userService).save(user);
        assertThat(user.getPassword()).isEqualTo("new-encoded-password");
        assertThat(user.getName()).isEqualTo("Changed");
        assertThat(user.getRole()).isEqualTo(Role.ROLE_ADMIN);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
