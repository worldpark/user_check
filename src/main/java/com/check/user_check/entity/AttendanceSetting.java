package com.check.user_check.entity;

import com.check.user_check.util.UUIDv6Generator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@EntityListeners(value = {AuditingEntityListener.class})
public class AttendanceSetting extends BaseEntity{

    @Id
    private UUID infoId;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotNull
    private LocalTime attendanceTime;

    @NotNull
    @LastModifiedDate
    private LocalDateTime updateAt;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    public void prePersist() {
        if (infoId == null) {
            infoId = UUIDv6Generator.generate();
        }
    }

    public void changeAttendanceTime(LocalTime attendanceTime, User user){
        this.attendanceTime = attendanceTime;
        this.user = user;
    }

    public void changeAttendancePosition(Double latitude, Double longitude, User user){
        this.latitude = latitude;
        this.longitude = longitude;
        this.user = user;
    }

}
