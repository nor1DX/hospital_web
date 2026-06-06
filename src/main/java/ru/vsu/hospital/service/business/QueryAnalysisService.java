package ru.vsu.hospital.service.business;

import ru.vsu.hospital.model.dto.QueryBenchmarkDto;

import java.util.List;

public interface QueryAnalysisService {
    List<QueryBenchmarkDto> runAllBenchmarks(boolean withExplain);
    void createIndexes();
    void dropIndexes();
}
