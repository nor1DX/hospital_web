package ru.vsu.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vsu.hospital.model.entity.Patient;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, String> {
    Optional<Patient> findByMedicalCardId(String medicalCardId);
}
