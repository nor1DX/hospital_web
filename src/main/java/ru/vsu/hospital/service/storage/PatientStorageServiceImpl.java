package ru.vsu.hospital.service.storage;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vsu.hospital.component.mapper.PatientMapper;
import ru.vsu.hospital.model.dto.PatientDto;
import ru.vsu.hospital.model.entity.Patient;
import ru.vsu.hospital.model.request.CreatePatientRequest;
import ru.vsu.hospital.repository.PatientRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PatientStorageServiceImpl implements PatientStorageService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Override
    public PatientDto getPatientById(String patientId) {
        return patientMapper.toDto(patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found")));
    }

    @Override
    public List<PatientDto> getPatients() {
        return patientRepository.findAll()
                .stream()
                .map(patientMapper::toDto)
                .toList();
    }

    @Override
    public boolean existsById(String patientId) {
        return patientRepository.existsById(patientId);
    }

    @Override
    public PatientDto createPatient(CreatePatientRequest request) {
        Patient patient = patientMapper.toEntity(request);
        return patientMapper.toDto(patientRepository.save(patient));
    }

    @Override
    public PatientDto updatePatient(PatientDto patientDto) {
        if (patientDto.getId() == null || patientDto.getId().isBlank()) {
            throw new IllegalArgumentException("Patient id must not be null");
        }

        Patient patient = patientRepository.findById(patientDto.getId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        patient.setFirstName(patientDto.getFirstName());
        patient.setLastName(patientDto.getLastName());
        patient.setDateOfBirth(patientDto.getDateOfBirth());
        patient.setMedicalCardId(patientDto.getMedicalCardId());

        return patientMapper.toDto(patientRepository.save(patient));
    }

    @Override
    public PatientDto deletePatient(String patientId) {
        PatientDto deletedPatient = getPatientById(patientId);
        patientRepository.deleteById(patientId);
        return deletedPatient;
    }

    @Override
    public PatientDto setMedicalCard(String patientId, String medicalCardId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        patient.setMedicalCardId(medicalCardId);
        return patientMapper.toDto(patientRepository.save(patient));
    }

    @Override
    public void changeMedicalCard(String medicalCardId, String newMedicalCardId) {
        patientRepository.findByMedicalCardId(medicalCardId).ifPresent(patient -> {
            patient.setMedicalCardId(newMedicalCardId);
            patientRepository.save(patient);
        });
    }

    @Override
    @Transactional
    public long createPatients(String namePrefix, long startIndex, int count) {
        String prefix = (namePrefix == null || namePrefix.isBlank()) ? "load-patient" : namePrefix;
        List<Patient> patients = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            patients.add(Patient.builder()
                    .firstName(prefix + "-" + (startIndex + i))
                    .lastName("Bulk")
                    .dateOfBirth("2000-01-01")
                    .build());
        }
        return patientRepository.saveAll(patients).size();
    }
}
