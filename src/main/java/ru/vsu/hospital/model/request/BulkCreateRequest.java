package ru.vsu.hospital.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulkCreateRequest {
    private String namePrefix;
    private long startIndex;
    private int count;
}
