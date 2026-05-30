package ru.vsu.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.vsu.hospital.model.dto.MedicalCardDetailsDto;
import ru.vsu.hospital.model.entity.MedicalCard;

import java.util.List;

public interface MedicalCardRepository extends JpaRepository<MedicalCard, String> {

    List<MedicalCard> findAllByDoctorId(String doctorId);

    @Query(value = """
            SELECT mc.id,
                   mc.diagnosis,
                   COALESCE(d.first_name || ' ' || d.last_name, 'Не назначен') AS doctorName,
                   p.first_name || ' ' || p.last_name AS patientName,
                   i.name AS illnessName
            FROM medical_cards mc
            LEFT JOIN doctors d ON mc.doctor_id = d.id
            LEFT JOIN patients p ON mc.patient_id = p.id
            LEFT JOIN illnesses i ON mc.illness_id = i.id
            """, nativeQuery = true)
    List<MedicalCardDetailsDto> findAllWithDetails();
}
