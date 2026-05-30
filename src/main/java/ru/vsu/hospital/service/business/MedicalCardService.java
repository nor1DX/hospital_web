package ru.vsu.hospital.service.business;

import ru.vsu.hospital.model.dto.MedicalCardDto;
import ru.vsu.hospital.model.request.CreateMedicalCardRequest;

import java.util.List;

public interface MedicalCardService {
    MedicalCardDto getMedicalCardById(String medicalCardId);
    List<MedicalCardDto> getMedicalCards();
    MedicalCardDto createMedicalCard(CreateMedicalCardRequest request);
    MedicalCardDto updateMedicalCard(MedicalCardDto medicalCardDto);
    MedicalCardDto deleteMedicalCardById(String medicalCardId);
    MedicalCardDto addDoctor(String medicalCardId, String doctorId);
    MedicalCardDto removeDoctor(String medicalCardId, String doctorId);
    MedicalCardDto addIllness(String medicalCardId, String illnessId);
    MedicalCardDto markAsRecovered(String medicalCardId);
}
