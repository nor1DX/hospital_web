package ru.vsu.hospital.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@Builder
public class MedicalCardDto {
    private String id;
    private String patientId;
    private String doctorId;
    private String illnessId;
    private String diagnosis;
    private Date treatmentStart;
    private Date expireAt;
}
