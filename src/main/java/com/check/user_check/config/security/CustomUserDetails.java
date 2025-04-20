package com.check.user_check.config.security;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

@Builder
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final String userName;
    private final String password;
    private final UUID userUuid;

    private final Collection<? extends GrantedAuthority> authorities;

    public UUID getUserUuid() {
        return userUuid;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }
}
