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

    @ManyToOne
    private Attendance attendance;

    @PrePersist
    public void prePersist() {
        if (attendanceTimeId == null) {
            attendanceTimeId = UUIDv6Generator.generate();
        }
    }
}
