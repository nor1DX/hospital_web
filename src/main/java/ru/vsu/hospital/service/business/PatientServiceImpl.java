package ru.vsu.hospital.service.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vsu.hospital.model.dto.PatientDto;
import ru.vsu.hospital.model.request.CreatePatientRequest;
import ru.vsu.hospital.service.storage.PatientStorageService;

import java.util.List;

@Service
@AllArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final PatientStorageService patientStorageService;

    @Override
    public PatientDto getPatientById(String patientId) {
        return patientStorageService.getPatientById(patientId);
    }

    @Override
    public List<PatientDto> getPatients() {
        return patientStorageService.getPatients();
    }

    @Override
    public PatientDto createPatient(CreatePatientRequest request) {
        return patientStorageService.createPatient(request);
    }

    @Override
    public PatientDto updatePatient(PatientDto patientDto) {
        return patientStorageService.updatePatient(patientDto);
    }

    @Override
    public PatientDto deletePatient(String patientId) {
        return patientStorageService.deletePatient(patientId);
    }
}
