package com.check.user_check.entity;

import com.check.user_check.util.UUIDv6Generator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "attendanceTargetUnique",
                        columnNames = {"attendance_date", "user_id"}
                )
        }
)
public class AttendanceTarget extends BaseEntity{

    @Id
    private UUID targetId;

    @NotNull
    private LocalDateTime attendanceDate;

    @ManyToOne
    @JoinColumn(name = "assigned_by")
    private User assignedUser;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    public void prePersist() {
        if (targetId == null) {
            targetId = UUIDv6Generator.generate();
        }
    }

    public void changeAttendanceDate(LocalDateTime attendanceDate){
        this.attendanceDate = attendanceDate;
    }
}
