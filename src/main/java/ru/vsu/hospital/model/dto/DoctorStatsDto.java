package ru.vsu.hospital.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DoctorStatsDto {
    private String specialization;
    private Long count;
}
