package ru.vsu.hospital.service.storage;

import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import ru.vsu.hospital.component.mapper.PatientMapper;
import ru.vsu.hospital.model.dto.PatientDto;
import ru.vsu.hospital.model.entity.Patient;
import ru.vsu.hospital.model.request.CreatePatientRequest;
import ru.vsu.hospital.repository.PatientRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class PatientStorageServiceImpl implements PatientStorageService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final MongoTemplate mongoTemplate;

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
        Query query = new Query().addCriteria(Criteria.where("_id").is(patientId));
        Update update = new Update().set("medicalCardId", medicalCardId);

        mongoTemplate.updateFirst(query, update, Patient.class);

        return patientMapper.toDto(mongoTemplate.findOne(query, Patient.class));
    }

    @Override
    public void changeMedicalCard(String medicalCardId, String newMedicalCardId) {
        Query query = new Query().addCriteria(Criteria.where("medicalCardId").is(medicalCardId));
        Update update = new Update().set("medicalCardId", newMedicalCardId);

        mongoTemplate.updateFirst(query, update, Patient.class);
    }
}
