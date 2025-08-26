package com.check.user_check.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "outbox_event")
@Getter
@Builder
public class OutboxEvent {

    @Id
    private UUID id;

    private String aggregateType;
    private UUID aggregateId;

    private String type;
    private String payload;
    private LocalDateTime occurredAt;

    private String jwt;

}
