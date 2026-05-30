package ru.vsu.hospital.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class BulkOperationResultDto {
    private long requested;
    private long processed;
}
