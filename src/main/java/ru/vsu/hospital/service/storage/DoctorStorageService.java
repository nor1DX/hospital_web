package ru.vsu.hospital.service.storage;

import ru.vsu.hospital.model.dto.DoctorDto;
import ru.vsu.hospital.model.dto.DoctorStatsDto;

import java.util.List;

public interface DoctorStorageService {
    DoctorDto getDoctorById(String doctorId);
    List<DoctorDto> getDoctors();
    List<DoctorDto> getDoctorsBySpecialization(String specialization);
    List<DoctorDto> getDoctorsSortedByLastName();
    List<DoctorStatsDto> getDoctorStats();
    DoctorDto createDoctor(DoctorDto doctorDto);
    DoctorDto updateDoctor(DoctorDto doctorDto);
    DoctorDto deleteDoctorById(String doctorId);
    boolean doctorExists(String doctorId);
    long createDoctors(String namePrefix, long startIndex, int count);
}
