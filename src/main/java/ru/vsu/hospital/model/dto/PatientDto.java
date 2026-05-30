package ru.vsu.hospital.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PatientDto {
    private String id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String medicalCardId;
}
