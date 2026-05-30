package ru.vsu.hospital.service.storage;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vsu.hospital.component.mapper.MedicalCardMapper;
import ru.vsu.hospital.model.dto.MedicalCardDetailsDto;
import ru.vsu.hospital.model.dto.MedicalCardDto;
import ru.vsu.hospital.model.entity.MedicalCard;
import ru.vsu.hospital.model.request.CreateMedicalCardRequest;
import ru.vsu.hospital.repository.MedicalCardRepository;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class MedicalCardStorageServiceImpl implements MedicalCardStorageService {
    private final PatientStorageService patientStorageService;
    private final MedicalCardRepository medicalCardRepository;
    private final MedicalCardMapper medicalCardMapper;

    @Override
    public MedicalCardDto getMedicalCardById(String medicalCardId) {
        return medicalCardMapper.toDto(
                medicalCardRepository.findById(medicalCardId)
                        .orElseThrow(() -> new RuntimeException("MedicalCard does not exist"))
        );
    }

    @Override
    public List<MedicalCardDto> getMedicalCards() {
        return medicalCardRepository.findAll()
                .stream()
                .map(medicalCardMapper::toDto)
                .toList();
    }

    @Override
    public List<MedicalCardDetailsDto> getMedicalCardsWithDetails() {
        return medicalCardRepository.findAllWithDetails();
    }

    @Override
    public boolean existsById(String medicalCardId) {
        return medicalCardRepository.existsById(medicalCardId);
    }

    @Override
    @Transactional
    public MedicalCardDto createMedicalCard(CreateMedicalCardRequest request) {
        MedicalCard medicalCard = medicalCardMapper.toEntity(request);
        medicalCard.setTreatmentStart(new Date());

        MedicalCard saved = medicalCardRepository.save(medicalCard);

        if (request.getPatientId() != null && !request.getPatientId().isBlank()) {
            patientStorageService.setMedicalCard(request.getPatientId(), saved.getId());
        }

        return medicalCardMapper.toDto(saved);
    }

    @Override
    public MedicalCardDto updateMedicalCard(MedicalCardDto medicalCardDto) {
        MedicalCard medicalCard = medicalCardRepository.findById(medicalCardDto.getId())
                .orElseThrow(() -> new RuntimeException("MedicalCard does not exist"));

        medicalCard.setPatientId(medicalCardDto.getPatientId());
        medicalCard.setDoctorId(medicalCardDto.getDoctorId());
        medicalCard.setIllnessId(medicalCardDto.getIllnessId());
        medicalCard.setDiagnosis(medicalCardDto.getDiagnosis());
        medicalCard.setExpireAt(medicalCardDto.getExpireAt());

        return medicalCardMapper.toDto(medicalCardRepository.save(medicalCard));
    }

    @Override
    @Transactional
    public MedicalCardDto deleteMedicalCardById(String medicalCardId) {
        MedicalCardDto deleted = getMedicalCardById(medicalCardId);
        patientStorageService.changeMedicalCard(medicalCardId, null);
        medicalCardRepository.deleteById(medicalCardId);
        return deleted;
    }

    @Override
    public void addDoctor(String medicalCardId, String doctorId) {
        MedicalCard card = medicalCardRepository.findById(medicalCardId)
                .orElseThrow(() -> new RuntimeException("MedicalCard does not exist"));
        card.setDoctorId(doctorId);
        medicalCardRepository.save(card);
    }

    @Override
    public void removeDoctor(String doctorId) {
        List<MedicalCard> cards = medicalCardRepository.findAllByDoctorId(doctorId);
        cards.forEach(card -> card.setDoctorId(null));
        medicalCardRepository.saveAll(cards);
    }

    @Override
    public void removeDoctorFromCard(String medicalCardId, String doctorId) {
        MedicalCard card = medicalCardRepository.findById(medicalCardId)
                .orElseThrow(() -> new RuntimeException("MedicalCard does not exist"));
        if (doctorId.equals(card.getDoctorId())) {
            card.setDoctorId(null);
            medicalCardRepository.save(card);
        }
    }

    @Override
    public void addIllness(String medicalCardId, String illnessId) {
        MedicalCard card = medicalCardRepository.findById(medicalCardId)
                .orElseThrow(() -> new RuntimeException("MedicalCard does not exist"));
        card.setIllnessId(illnessId);
        medicalCardRepository.save(card);
    }

    @Override
    public void markAsRecovered(String medicalCardId) {
        MedicalCard card = medicalCardRepository.findById(medicalCardId)
                .orElseThrow(() -> new RuntimeException("MedicalCard does not exist"));
        card.setExpireAt(new Date());
        medicalCardRepository.save(card);
    }
}
