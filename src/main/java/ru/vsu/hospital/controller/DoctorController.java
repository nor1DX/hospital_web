package ru.vsu.hospital.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.vsu.hospital.model.dto.DoctorDto;
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
    public List<DoctorDto> getDoctors() {
        return doctorService.getDoctors();
    }

    @PostMapping
    public DoctorDto createDoctor(@RequestBody DoctorDto doctorDto) {
        return doctorService.createDoctor(doctorDto);
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
