package ru.vsu.hospital.service.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vsu.hospital.model.dto.MedicalCardDto;
import ru.vsu.hospital.model.request.CreateMedicalCardRequest;
import ru.vsu.hospital.service.storage.MedicalCardStorageService;

import java.util.List;

@Service
@AllArgsConstructor
public class MedicalCardServiceImpl implements MedicalCardService {
    private final MedicalCardStorageService medicalCardStorageService;

    @Override
    public MedicalCardDto getMedicalCardById(String medicalCardId) {
        return medicalCardStorageService.getMedicalCardById(medicalCardId);
    }

    @Override
    public List<MedicalCardDto> getMedicalCards() {
        return medicalCardStorageService.getMedicalCards();
    }

    @Override
    public MedicalCardDto createMedicalCard(CreateMedicalCardRequest request) {
        return medicalCardStorageService.createMedicalCard(request);
    }

    @Override
    public MedicalCardDto updateMedicalCard(MedicalCardDto medicalCardDto) {
        return medicalCardStorageService.updateMedicalCard(medicalCardDto);
    }

    @Override
    public MedicalCardDto deleteMedicalCardById(String medicalCardId) {
        return medicalCardStorageService.deleteMedicalCardById(medicalCardId);
    }

    @Override
    public MedicalCardDto addDoctor(String medicalCardId, String doctorId) {
        medicalCardStorageService.addDoctor(medicalCardId, doctorId);
        return medicalCardStorageService.getMedicalCardById(medicalCardId);
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
        return medicalCardStorageService.getMedicalCardById(medicalCardId);
    }
}
