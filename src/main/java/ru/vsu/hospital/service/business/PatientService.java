package ru.vsu.hospital.service.business;

import ru.vsu.hospital.model.dto.PatientDto;
import ru.vsu.hospital.model.request.CreatePatientRequest;

import java.util.List;

public interface PatientService {
    PatientDto getPatientById(String patientId);
    List<PatientDto> getPatients();
    PatientDto createPatient(CreatePatientRequest request);
    PatientDto updatePatient(PatientDto patientDto);
    PatientDto deletePatient(String patientId);
}
