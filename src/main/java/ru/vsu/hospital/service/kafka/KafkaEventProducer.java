package ru.vsu.hospital.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.vsu.hospital.model.dto.HospitalEvent;

@Service
@AllArgsConstructor
public class KafkaEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void send(String topic, String eventType, String entityType, String entityId, Object payload, int version) {
        try {
            HospitalEvent event = HospitalEvent.builder()
                    .eventType(eventType)
                    .entityType(entityType)
                    .entityId(entityId)
                    .payload(objectMapper.writeValueAsString(payload))
                    .version(version)
                    .timestamp(System.currentTimeMillis())
                    .build();
            kafkaTemplate.send(topic, entityId, objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize Kafka event", e);
        }
    }
}
