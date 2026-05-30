package ru.vsu.hospital.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.vsu.hospital.model.entity.MedicalCard;

public interface MedicalCardRepository extends MongoRepository<MedicalCard, String> {
}
