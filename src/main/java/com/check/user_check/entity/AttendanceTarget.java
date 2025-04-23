package com.check.user_check.entity;

import com.check.user_check.enumeratedType.AttendanceAuth;
import com.check.user_check.util.UUIDv6Generator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceTarget {

    @Id
    private UUID targetId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AttendanceAuth auth;

    @ManyToOne
    private User user;

    @ManyToOne
    private Attendance attendance;

    @PrePersist
    public void prePersist() {
        if (targetId == null) {
            targetId = UUIDv6Generator.generate();
        }
    }
}
