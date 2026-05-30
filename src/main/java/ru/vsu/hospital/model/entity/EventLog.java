package ru.vsu.hospital.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "event_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String topic;
    private String eventType;
    private String entityType;
    private String entityId;

    @Column(columnDefinition = "TEXT")
    private String payload;

    private int version;

    @Column(name = "received_at")
    private LocalDateTime receivedAt;
}
