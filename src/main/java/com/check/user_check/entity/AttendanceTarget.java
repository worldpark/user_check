package com.check.user_check.entity;

import com.check.user_check.enumeratedType.AttendanceAuth;
import com.check.user_check.enumeratedType.TargetStatus;
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

    @NotNull
    @Enumerated(EnumType.STRING)
    private TargetStatus targetStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendance_id")
    private Attendance attendance;

    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
    }

    @PrePersist
    public void prePersist() {
        if (targetId == null) {
            targetId = UUIDv6Generator.generate();
        }
    }
}
