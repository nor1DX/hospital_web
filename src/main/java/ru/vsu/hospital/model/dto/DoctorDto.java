package ru.vsu.hospital.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DoctorDto {
    private String id;
    private String firstName;
    private String lastName;
    private String specialization;
}
