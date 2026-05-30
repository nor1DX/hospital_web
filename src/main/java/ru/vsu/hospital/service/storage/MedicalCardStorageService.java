package ru.vsu.hospital.service.storage;

import ru.vsu.hospital.model.dto.MedicalCardDetailsDto;
import ru.vsu.hospital.model.dto.MedicalCardDto;
import ru.vsu.hospital.model.request.CreateMedicalCardRequest;

import java.util.List;

public interface MedicalCardStorageService {
    MedicalCardDto getMedicalCardById(String medicalCardId);
    List<MedicalCardDto> getMedicalCards();
    List<MedicalCardDetailsDto> getMedicalCardsWithDetails();
    boolean existsById(String medicalCardId);
    MedicalCardDto createMedicalCard(CreateMedicalCardRequest request);
    MedicalCardDto updateMedicalCard(MedicalCardDto medicalCardDto);
    MedicalCardDto deleteMedicalCardById(String medicalCardId);
    void addDoctor(String medicalCardId, String doctorId);
    void removeDoctor(String doctorId);
    void removeDoctorFromCard(String medicalCardId, String doctorId);
    void addIllness(String medicalCardId, String illnessId);
    void markAsRecovered(String medicalCardId);
}
