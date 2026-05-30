package ru.vsu.hospital.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class IllnessDto {
    private String id;
    private String name;
    private String description;
    private String severity;
}
