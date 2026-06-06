package ru.vsu.hospital.service.business;

import ru.vsu.hospital.model.dto.EventLogDto;
import ru.vsu.hospital.model.entity.EventCounter;
import ru.vsu.hospital.model.request.SendEventRequest;

import java.util.List;

public interface EventService {

    void sendEvent(SendEventRequest request);

    // Task 11: прямая запись в БД минуя Kafka (второй источник)
    void saveDirectly(SendEventRequest request);

    List<EventLogDto> getEventLog();

    List<EventCounter> getCounters();
}
