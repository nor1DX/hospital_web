package ru.vsu.hospital.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.vsu.hospital.model.dto.HospitalEvent;
import ru.vsu.hospital.service.storage.EventLogStorageService;

@Service
@AllArgsConstructor
public class KafkaEventConsumer {

    private final EventLogStorageService eventLogStorageService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = {
            "hospital.doctors",
            "hospital.patients",
            "hospital.medical-cards"
    }, groupId = "hospital-consumer")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            HospitalEvent event = objectMapper.readValue(record.value(), HospitalEvent.class);
            eventLogStorageService.saveEvent(record.topic(), event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize Kafka event", e);
        }
    }
}
