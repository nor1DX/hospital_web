package ru.vsu.hospital.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("medical_cards")
@Data
@AllArgsConstructor
@Builder
public class MedicalCard {
    @Id
    private String id;
    private String patientId;
    private String doctorId;
    private String illnessId;
    private String diagnosis;
    private Date treatmentStart;
    private Date expireAt;
}
