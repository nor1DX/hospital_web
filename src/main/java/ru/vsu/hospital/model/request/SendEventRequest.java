package ru.vsu.hospital.model.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendEventRequest {
    private String topic;
    private String eventType;
    private String entityType;
    private String entityId;
    private String payload;
    private int version;
}
