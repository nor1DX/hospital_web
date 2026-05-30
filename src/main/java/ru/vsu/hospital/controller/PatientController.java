package ru.vsu.hospital.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.vsu.hospital.model.dto.BulkOperationResultDto;
import ru.vsu.hospital.model.dto.PatientDto;
import ru.vsu.hospital.model.request.BulkCreateRequest;
import ru.vsu.hospital.model.request.CreatePatientRequest;
import ru.vsu.hospital.service.business.PatientService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/patients")
public class PatientController {
    private final PatientService patientService;

    @GetMapping("/{patientId}")
    public PatientDto getPatient(@PathVariable String patientId) {
        return patientService.getPatientById(patientId);
    }

    @GetMapping
    public List<PatientDto> getPatients() {
        return patientService.getPatients();
    }

    @PostMapping
    public PatientDto createPatient(@RequestBody CreatePatientRequest request) {
        return patientService.createPatient(request);
    }

    @PostMapping("/bulk")
    public BulkOperationResultDto createPatients(@RequestBody BulkCreateRequest request) {
        long processed = patientService.createPatients(request.getNamePrefix(), request.getStartIndex(), request.getCount());
        return BulkOperationResultDto.builder()
                .requested(request.getCount())
                .processed(processed)
                .build();
    }

    @PutMapping
    public PatientDto updatePatient(@RequestBody PatientDto patientDto) {
        return patientService.updatePatient(patientDto);
    }

    @DeleteMapping("/{patientId}")
    public PatientDto deletePatient(@PathVariable String patientId) {
        return patientService.deletePatient(patientId);
    }
}
