package ru.vsu.hospital.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.vsu.hospital.model.entity.Doctor;

public interface DoctorRepository extends MongoRepository<Doctor, String> {
}
