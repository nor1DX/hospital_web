package ru.vsu.hospital.service.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vsu.hospital.model.dto.DoctorDto;
import ru.vsu.hospital.service.storage.DoctorStorageService;

import java.util.List;

@Service
@AllArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorStorageService doctorStorageService;

    @Override
    public DoctorDto getDoctorById(String doctorId) {
        return doctorStorageService.getDoctorById(doctorId);
    }

    @Override
    public List<DoctorDto> getDoctors() {
        return doctorStorageService.getDoctors();
    }

    @Override
    public DoctorDto createDoctor(DoctorDto doctorDto) {
        return doctorStorageService.createDoctor(doctorDto);
    }

    @Override
    public DoctorDto updateDoctor(DoctorDto doctorDto) {
        return doctorStorageService.updateDoctor(doctorDto);
    }

    @Override
    public DoctorDto deleteDoctorById(String doctorId) {
        return doctorStorageService.deleteDoctorById(doctorId);
    }
}
