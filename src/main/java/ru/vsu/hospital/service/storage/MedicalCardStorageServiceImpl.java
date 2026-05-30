package ru.vsu.hospital.service.storage;

import com.mongodb.client.result.UpdateResult;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vsu.hospital.component.mapper.MedicalCardMapper;
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
    private final MongoTemplate mongoTemplate;

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
    public UpdateResult addDoctor(String medicalCardId, String doctorId) {
        Query query = new Query().addCriteria(Criteria.where("_id").is(medicalCardId));
        Update update = new Update().set("doctorId", doctorId);
        return mongoTemplate.updateFirst(query, update, MedicalCard.class);
    }

    @Override
    public UpdateResult removeDoctor(String doctorId) {
        Query query = new Query().addCriteria(Criteria.where("doctorId").is(doctorId));
        Update update = new Update().set("doctorId", null);
        return mongoTemplate.updateMulti(query, update, MedicalCard.class);
    }

    @Override
    public UpdateResult removeDoctorFromCard(String medicalCardId, String doctorId) {
        Query query = new Query()
                .addCriteria(Criteria.where("_id").is(medicalCardId))
                .addCriteria(Criteria.where("doctorId").is(doctorId));
        Update update = new Update().set("doctorId", null);
        return mongoTemplate.updateFirst(query, update, MedicalCard.class);
    }

    @Override
    public UpdateResult addIllness(String medicalCardId, String illnessId) {
        Query query = new Query().addCriteria(Criteria.where("_id").is(medicalCardId));
        Update update = new Update().set("illnessId", illnessId);
        return mongoTemplate.updateFirst(query, update, MedicalCard.class);
    }

    @Override
    public UpdateResult markAsRecovered(String medicalCardId) {
        Query query = new Query().addCriteria(Criteria.where("_id").is(medicalCardId));
        Update update = new Update().set("expireAt", new Date());
        return mongoTemplate.updateFirst(query, update, MedicalCard.class);
    }
}
