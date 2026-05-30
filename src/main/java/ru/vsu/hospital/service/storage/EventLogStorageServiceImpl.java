package ru.vsu.hospital.service.storage;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vsu.hospital.model.dto.HospitalEvent;
import ru.vsu.hospital.model.entity.EventCounter;
import ru.vsu.hospital.model.entity.EventLog;
import ru.vsu.hospital.repository.EventCounterRepository;
import ru.vsu.hospital.repository.EventLogRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class EventLogStorageServiceImpl implements EventLogStorageService {

    private final EventLogRepository eventLogRepository;
    private final EventCounterRepository eventCounterRepository;

    @Override
    @Transactional
    public void saveEvent(String topic, HospitalEvent event) {
        eventLogRepository.save(EventLog.builder()
                .topic(topic)
                .eventType(event.getEventType())
                .entityType(event.getEntityType())
                .entityId(event.getEntityId())
                .payload(event.getPayload())
                .version(event.getVersion())
                .receivedAt(LocalDateTime.now())
                .build());

        EventCounter counter = eventCounterRepository.findByTopicWithLock(topic)
                .orElse(EventCounter.builder().topic(topic).messageCount(0).build());
        counter.setMessageCount(counter.getMessageCount() + 1);
        eventCounterRepository.save(counter);
    }

    @Override
    public List<EventLog> getAll() {
        return eventLogRepository.findAllByOrderByReceivedAtDesc();
    }

    @Override
    public List<EventCounter> getCounters() {
        return eventCounterRepository.findAll();
    }
}
