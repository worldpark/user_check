package com.check.user_check.entity;

import com.check.user_check.util.UUIDv6Generator;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
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
