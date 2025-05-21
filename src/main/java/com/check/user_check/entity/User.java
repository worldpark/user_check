package com.check.user_check.entity;

import com.check.user_check.enumeratedType.Role;
import com.check.user_check.util.UUIDv6Generator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User {

    @Id
    private UUID uid;

    @NotNull
    @Column(unique = true)
    private String userId;

    @NotNull
    private String password;
    @NotNull
    private String userName;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @PrePersist
    public void prePersist() {
        if (uid == null) {
            uid = UUIDv6Generator.generate();
        }
    }

}
