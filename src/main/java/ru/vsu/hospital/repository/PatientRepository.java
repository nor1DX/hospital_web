package ru.vsu.hospital.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.vsu.hospital.model.entity.Patient;

public interface PatientRepository extends MongoRepository<Patient, String> {
}
