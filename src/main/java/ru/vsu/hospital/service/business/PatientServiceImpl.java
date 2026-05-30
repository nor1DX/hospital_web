package ru.vsu.hospital.service.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vsu.hospital.config.KafkaConfig;
import ru.vsu.hospital.model.dto.PatientDto;
import ru.vsu.hospital.model.request.CreatePatientRequest;
import ru.vsu.hospital.service.kafka.KafkaEventProducer;
import ru.vsu.hospital.service.storage.PatientStorageService;

import java.util.List;

@Service
@AllArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final PatientStorageService patientStorageService;
    private final KafkaEventProducer kafkaEventProducer;

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
        PatientDto created = patientStorageService.createPatient(request);
        kafkaEventProducer.send(KafkaConfig.PATIENTS_TOPIC, "CREATED", "PATIENT", created.getId(), created, 1);
        return created;
    }

    @Override
    public PatientDto updatePatient(PatientDto patientDto) {
        PatientDto updated = patientStorageService.updatePatient(patientDto);
        kafkaEventProducer.send(KafkaConfig.PATIENTS_TOPIC, "UPDATED", "PATIENT", updated.getId(), updated, 1);
        return updated;
    }

    @Override
    public PatientDto deletePatient(String patientId) {
        PatientDto deleted = patientStorageService.deletePatient(patientId);
        kafkaEventProducer.send(KafkaConfig.PATIENTS_TOPIC, "DELETED", "PATIENT", patientId, deleted, 1);
        return deleted;
    }

    @Override
    public long createPatients(String namePrefix, long startIndex, int count) {
        return patientStorageService.createPatients(namePrefix, startIndex, count);
    }
}
