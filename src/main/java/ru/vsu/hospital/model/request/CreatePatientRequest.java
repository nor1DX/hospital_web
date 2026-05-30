package ru.vsu.hospital.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePatientRequest {
    private String firstName;
    private String lastName;
    private String dateOfBirth;
}
