package ru.vsu.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vsu.hospital.model.entity.EventLog;

import java.util.List;

public interface EventLogRepository extends JpaRepository<EventLog, String> {

    List<EventLog> findAllByOrderByReceivedAtDesc();
}
