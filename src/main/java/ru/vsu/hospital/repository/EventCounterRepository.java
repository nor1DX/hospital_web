package ru.vsu.hospital.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.vsu.hospital.model.entity.EventCounter;

import java.util.Optional;

public interface EventCounterRepository extends JpaRepository<EventCounter, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ec FROM EventCounter ec WHERE ec.topic = :topic")
    Optional<EventCounter> findByTopicWithLock(@Param("topic") String topic);
}
