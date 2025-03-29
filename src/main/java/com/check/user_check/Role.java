package com.check.user_check;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ROLE_DEV("ROLE_DEV")
    , ROLE_ADMIN("ROLE_ADMIN")
    , ROLE_USER("ROLE_USER");

    private final String roleName;
}
