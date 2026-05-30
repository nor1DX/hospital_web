package ru.vsu.hospital.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("patients")
@Data
@AllArgsConstructor
@Builder
public class Patient {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String medicalCardId;
}
