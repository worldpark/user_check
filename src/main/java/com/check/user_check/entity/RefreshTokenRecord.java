package com.check.user_check.entity;

import com.check.user_check.util.UUIDv6Generator;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RefreshTokenRecord extends BaseEntity{

    @Id
    private UUID tokenId;

    @NotNull
    private String username;

    @NotNull
    private String tokenValue;

    @NotNull
    private LocalDateTime expireAt;

    @PrePersist
    public void prePersist() {
        if (tokenId == null) {
            tokenId = UUIDv6Generator.generate();
        }
    }
}
