package ru.vsu.hospital.service.storage;

import ru.vsu.hospital.model.dto.PatientDto;
import ru.vsu.hospital.model.request.CreatePatientRequest;

import java.util.List;

public interface PatientStorageService {
    PatientDto getPatientById(String patientId);
    List<PatientDto> getPatients();
    boolean existsById(String patientId);
    PatientDto createPatient(CreatePatientRequest request);
    PatientDto updatePatient(PatientDto patientDto);
    PatientDto deletePatient(String patientId);
    PatientDto setMedicalCard(String patientId, String medicalCardId);
    void changeMedicalCard(String medicalCardId, String newMedicalCardId);
    long createPatients(String namePrefix, long startIndex, int count);
}
