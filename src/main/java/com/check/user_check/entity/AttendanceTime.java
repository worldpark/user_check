package com.check.user_check.entity;

import com.check.user_check.util.UUIDv6Generator;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceTime {

    @Id
    private UUID attendanceTimeId;

    private LocalDateTime attendanceDatetime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendance_id")
    private Attendance attendance;

    @PrePersist
    public void prePersist() {
        if (attendanceTimeId == null) {
            attendanceTimeId = UUIDv6Generator.generate();
        }
    }
}
