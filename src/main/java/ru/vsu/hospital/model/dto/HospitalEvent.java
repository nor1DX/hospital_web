package ru.vsu.hospital.model.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HospitalEvent {
    private String eventType;
    private String entityType;
    private String entityId;
    private String payload;
    private int version;
    private long timestamp;
}
