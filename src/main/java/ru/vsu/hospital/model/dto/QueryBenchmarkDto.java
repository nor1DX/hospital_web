package ru.vsu.hospital.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QueryBenchmarkDto {
    private String queryType;
    private String description;
    private long executionTimeMs;
    private List<String> explainPlan;
}
