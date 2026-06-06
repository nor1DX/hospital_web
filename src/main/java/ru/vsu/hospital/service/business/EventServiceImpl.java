package ru.vsu.hospital.service.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vsu.hospital.model.dto.EventLogDto;
import ru.vsu.hospital.model.dto.HospitalEvent;
import ru.vsu.hospital.model.entity.EventCounter;
import ru.vsu.hospital.model.entity.EventLog;
import ru.vsu.hospital.model.request.SendEventRequest;
import ru.vsu.hospital.service.kafka.KafkaEventProducer;
import ru.vsu.hospital.service.storage.EventLogStorageService;

import java.util.List;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {

    private final KafkaEventProducer kafkaEventProducer;
    private final EventLogStorageService eventLogStorageService;

    @Override
    public void saveDirectly(SendEventRequest request) {
        eventLogStorageService.saveEvent(request.getTopic(), HospitalEvent.builder()
                .eventType(request.getEventType())
                .entityType(request.getEntityType())
                .entityId(request.getEntityId())
                .payload(request.getPayload())
                .version(request.getVersion())
                .timestamp(System.currentTimeMillis())
                .build());
    }

    @Override
    public void sendEvent(SendEventRequest request) {
        kafkaEventProducer.send(
                request.getTopic(),
                request.getEventType(),
                request.getEntityType(),
                request.getEntityId(),
                request.getPayload(),
                request.getVersion()
        );
    }

    @Override
    public List<EventLogDto> getEventLog() {
        return eventLogStorageService.getAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<EventCounter> getCounters() {
        return eventLogStorageService.getCounters();
    }

    private EventLogDto toDto(EventLog log) {
        return EventLogDto.builder()
                .id(log.getId())
                .topic(log.getTopic())
                .eventType(log.getEventType())
                .entityType(log.getEntityType())
                .entityId(log.getEntityId())
                .payload(log.getPayload())
                .version(log.getVersion())
                .receivedAt(log.getReceivedAt())
                .build();
    }
}
