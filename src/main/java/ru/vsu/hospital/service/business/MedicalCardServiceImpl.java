package ru.vsu.hospital.service.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vsu.hospital.config.KafkaConfig;
import ru.vsu.hospital.model.dto.MedicalCardDetailsDto;
import ru.vsu.hospital.model.dto.MedicalCardDto;
import ru.vsu.hospital.model.request.CreateMedicalCardRequest;
import ru.vsu.hospital.service.kafka.KafkaEventProducer;
import ru.vsu.hospital.service.storage.MedicalCardStorageService;

import java.util.List;

@Service
@AllArgsConstructor
public class MedicalCardServiceImpl implements MedicalCardService {
    private final MedicalCardStorageService medicalCardStorageService;
    private final KafkaEventProducer kafkaEventProducer;

    @Override
    public MedicalCardDto getMedicalCardById(String medicalCardId) {
        return medicalCardStorageService.getMedicalCardById(medicalCardId);
    }

    @Override
    public List<MedicalCardDto> getMedicalCards() {
        return medicalCardStorageService.getMedicalCards();
    }

    @Override
    public List<MedicalCardDetailsDto> getMedicalCardsWithDetails() {
        return medicalCardStorageService.getMedicalCardsWithDetails();
    }

    @Override
    public MedicalCardDto createMedicalCard(CreateMedicalCardRequest request) {
        MedicalCardDto created = medicalCardStorageService.createMedicalCard(request);
        kafkaEventProducer.send(KafkaConfig.MEDICAL_CARDS_TOPIC, "CREATED", "MEDICAL_CARD", created.getId(), created, 1);
        return created;
    }

    @Override
    public MedicalCardDto updateMedicalCard(MedicalCardDto medicalCardDto) {
        return medicalCardStorageService.updateMedicalCard(medicalCardDto);
    }

    @Override
    public MedicalCardDto deleteMedicalCardById(String medicalCardId) {
        MedicalCardDto deleted = medicalCardStorageService.deleteMedicalCardById(medicalCardId);
        kafkaEventProducer.send(KafkaConfig.MEDICAL_CARDS_TOPIC, "DELETED", "MEDICAL_CARD", medicalCardId, deleted, 1);
        return deleted;
    }

    @Override
    public MedicalCardDto addDoctor(String medicalCardId, String doctorId) {
        medicalCardStorageService.addDoctor(medicalCardId, doctorId);
        MedicalCardDto updated = medicalCardStorageService.getMedicalCardById(medicalCardId);
        kafkaEventProducer.send(KafkaConfig.MEDICAL_CARDS_TOPIC, "DOCTOR_ASSIGNED", "MEDICAL_CARD", medicalCardId, updated, 1);
        return updated;
    }

    @Override
    public MedicalCardDto removeDoctor(String medicalCardId, String doctorId) {
        medicalCardStorageService.removeDoctorFromCard(medicalCardId, doctorId);
        return medicalCardStorageService.getMedicalCardById(medicalCardId);
    }

    @Override
    public MedicalCardDto addIllness(String medicalCardId, String illnessId) {
        medicalCardStorageService.addIllness(medicalCardId, illnessId);
        return medicalCardStorageService.getMedicalCardById(medicalCardId);
    }

    @Override
    public MedicalCardDto markAsRecovered(String medicalCardId) {
        medicalCardStorageService.markAsRecovered(medicalCardId);
        MedicalCardDto recovered = medicalCardStorageService.getMedicalCardById(medicalCardId);
        kafkaEventProducer.send(KafkaConfig.MEDICAL_CARDS_TOPIC, "RECOVERED", "MEDICAL_CARD", medicalCardId, recovered, 1);
        return recovered;
    }
}
