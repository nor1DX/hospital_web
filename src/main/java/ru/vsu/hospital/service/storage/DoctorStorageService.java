package ru.vsu.hospital.service.storage;

import ru.vsu.hospital.model.dto.DoctorDto;

import java.util.List;

public interface DoctorStorageService {
    DoctorDto getDoctorById(String doctorId);
    List<DoctorDto> getDoctors();
    DoctorDto createDoctor(DoctorDto doctorDto);
    DoctorDto updateDoctor(DoctorDto doctorDto);
    DoctorDto deleteDoctorById(String doctorId);
    boolean doctorExists(String doctorId);
}
