package com.check.user_check.entity;

import com.check.user_check.enumeratedType.AttendanceAuth;
import com.check.user_check.util.UUIDv6Generator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Attendance {

    @Id
    private UUID attendanceId;

    private String attendanceName;

    @OneToMany(mappedBy = "attendance")
    List<AttendanceTarget> attendanceTargets;

    @PrePersist
    public void prePersist() {
        if (attendanceId == null) {
            attendanceId = UUIDv6Generator.generate();
        }
    }
}
