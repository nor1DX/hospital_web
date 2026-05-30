package ru.vsu.hospital.service.storage;

import com.mongodb.client.result.UpdateResult;
import ru.vsu.hospital.model.dto.MedicalCardDto;
import ru.vsu.hospital.model.request.CreateMedicalCardRequest;

import java.util.List;

public interface MedicalCardStorageService {
    MedicalCardDto getMedicalCardById(String medicalCardId);
    List<MedicalCardDto> getMedicalCards();
    boolean existsById(String medicalCardId);
    MedicalCardDto createMedicalCard(CreateMedicalCardRequest request);
    MedicalCardDto updateMedicalCard(MedicalCardDto medicalCardDto);
    MedicalCardDto deleteMedicalCardById(String medicalCardId);
    UpdateResult addDoctor(String medicalCardId, String doctorId);
    UpdateResult removeDoctor(String doctorId);
    UpdateResult removeDoctorFromCard(String medicalCardId, String doctorId);
    UpdateResult addIllness(String medicalCardId, String illnessId);
    UpdateResult markAsRecovered(String medicalCardId);
}
