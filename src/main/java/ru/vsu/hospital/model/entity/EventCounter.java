package ru.vsu.hospital.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "event_counters")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventCounter {

    @Id
    private String topic;

    @Column(name = "message_count")
    private long messageCount;
}
