package ru.vsu.hospital.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMedicalCardRequest {
    private String patientId;
    private String illnessId;
    private String diagnosis;
}
