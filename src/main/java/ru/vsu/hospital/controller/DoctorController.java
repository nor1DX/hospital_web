package ru.vsu.hospital.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.vsu.hospital.model.dto.BulkOperationResultDto;
import ru.vsu.hospital.model.dto.DoctorDto;
import ru.vsu.hospital.model.dto.DoctorStatsDto;
import ru.vsu.hospital.model.request.BulkCreateRequest;
import ru.vsu.hospital.service.business.DoctorService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    @GetMapping("/{doctorId}")
    public DoctorDto getDoctor(@PathVariable String doctorId) {
        return doctorService.getDoctorById(doctorId);
    }

    @GetMapping
    public List<DoctorDto> getDoctors(@RequestParam(required = false) String specialization,
                                      @RequestParam(required = false) String sort) {
        if (specialization != null) {
            return doctorService.getDoctorsBySpecialization(specialization);
        }
        if ("lastName".equals(sort)) {
            return doctorService.getDoctorsSortedByLastName();
        }
        return doctorService.getDoctors();
    }

    @GetMapping("/stats")
    public List<DoctorStatsDto> getDoctorStats() {
        return doctorService.getDoctorStats();
    }

    @PostMapping
    public DoctorDto createDoctor(@RequestBody DoctorDto doctorDto) {
        return doctorService.createDoctor(doctorDto);
    }

    @PostMapping("/bulk")
    public BulkOperationResultDto createDoctors(@RequestBody BulkCreateRequest request) {
        long processed = doctorService.createDoctors(request.getNamePrefix(), request.getStartIndex(), request.getCount());
        return BulkOperationResultDto.builder()
                .requested(request.getCount())
                .processed(processed)
                .build();
    }

    @PutMapping
    public DoctorDto updateDoctor(@RequestBody DoctorDto doctorDto) {
        return doctorService.updateDoctor(doctorDto);
    }

    @DeleteMapping("/{doctorId}")
    public DoctorDto deleteDoctor(@PathVariable String doctorId) {
        return doctorService.deleteDoctorById(doctorId);
    }
}
