package com.check.user_check.config.security;

import com.check.user_check.entity.User;
import com.check.user_check.service.response.basic.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUserId(username);

        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(user.getRole().getRoleName())
        );

        return CustomUserDetails.builder()
                .userName(user.getUserId())
                .password(user.getPassword())
                .authorities(authorities)
                .userUuid(user.getUid())
                .build();
    }
}
