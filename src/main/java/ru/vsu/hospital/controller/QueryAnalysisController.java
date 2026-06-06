package ru.vsu.hospital.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.vsu.hospital.model.dto.QueryBenchmarkDto;
import ru.vsu.hospital.service.business.QueryAnalysisService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/query-analysis")
public class QueryAnalysisController {

    private final QueryAnalysisService queryAnalysisService;

    @GetMapping("/benchmark")
    public List<QueryBenchmarkDto> runBenchmark(
            @RequestParam(defaultValue = "false") boolean withExplain) {
        return queryAnalysisService.runAllBenchmarks(withExplain);
    }

    @PostMapping("/indexes")
    public void createIndexes() {
        queryAnalysisService.createIndexes();
    }

    @DeleteMapping("/indexes")
    public void dropIndexes() {
        queryAnalysisService.dropIndexes();
    }
}
