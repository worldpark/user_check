package com.check.user_check.entity;

import com.check.user_check.enumeratedType.AttendanceStatus;
import com.check.user_check.util.UUIDv6Generator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "attendanceUnique",
                        columnNames = {"attendance_date", "user_id"}
                )
        }
)
public class Attendance {

    @Id
    private UUID attendanceId;

    @NotNull
    private LocalDateTime attendanceDate;
    private LocalDateTime checkTime;

    @Enumerated(EnumType.STRING)
    @NotNull
    private AttendanceStatus status;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    public void prePersist() {
        if (attendanceId == null) {
            attendanceId = UUIDv6Generator.generate();
        }
    }

    public void changeStatus(AttendanceStatus status){
        this.status = status;
    }

    public void changeCheckTime(LocalDateTime checkTime){
        this.checkTime = checkTime;
    }

    public void changeAttendanceDate(LocalDateTime attendanceDate){
        this.attendanceDate = attendanceDate;
    }
}
