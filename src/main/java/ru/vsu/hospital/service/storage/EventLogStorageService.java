package ru.vsu.hospital.service.storage;

import ru.vsu.hospital.model.dto.HospitalEvent;
import ru.vsu.hospital.model.entity.EventCounter;
import ru.vsu.hospital.model.entity.EventLog;

import java.util.List;

public interface EventLogStorageService {

    void saveEvent(String topic, HospitalEvent event);

    List<EventLog> getAll();

    List<EventCounter> getCounters();
}
