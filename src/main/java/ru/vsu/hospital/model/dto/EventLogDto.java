package ru.vsu.hospital.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventLogDto {
    private String id;
    private String topic;
    private String eventType;
    private String entityType;
    private String entityId;
    private String payload;
    private int version;
    private LocalDateTime receivedAt;
}
