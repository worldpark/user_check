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

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceStatus {

    @Id
    private UUID statusId;

    private String checkStatus;
    private LocalDateTime checkTime;
    private LocalDateTime updateTime;

    @ManyToOne
    private User user;

    @ManyToOne
    private AttendanceTime attendanceTime;

    @PrePersist
    public void prePersist() {
        if (statusId == null) {
            statusId = UUIDv6Generator.generate();
        }
    }
}
