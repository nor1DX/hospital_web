package ru.vsu.hospital.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.vsu.hospital.model.dto.IllnessDto;
import ru.vsu.hospital.service.business.IllnessService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/illnesses")
public class IllnessController {
    private final IllnessService illnessService;

    @GetMapping("/{illnessId}")
    public IllnessDto getIllness(@PathVariable String illnessId) {
        return illnessService.getIllnessById(illnessId);
    }

    @GetMapping
    public List<IllnessDto> getIllnesses() {
        return illnessService.getIllnesses();
    }

    @GetMapping("/search")
    public List<IllnessDto> searchIllnesses(@RequestParam String text) {
        return illnessService.searchByDescription(text);
    }

    @PostMapping
    public IllnessDto createIllness(@RequestBody IllnessDto illnessDto) {
        return illnessService.createIllness(illnessDto);
    }

    @PutMapping
    public IllnessDto updateIllness(@RequestBody IllnessDto illnessDto) {
        return illnessService.updateIllness(illnessDto);
    }

    @DeleteMapping("/{illnessId}")
    public IllnessDto deleteIllness(@PathVariable String illnessId) {
        return illnessService.deleteIllnessById(illnessId);
    }
}
