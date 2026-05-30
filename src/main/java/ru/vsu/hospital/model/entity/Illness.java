package ru.vsu.hospital.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("illnesses")
@Data
@AllArgsConstructor
@Builder
public class Illness {
    @Id
    private String id;
    private String name;
    @TextIndexed
    private String description;
    private String severity;
}
