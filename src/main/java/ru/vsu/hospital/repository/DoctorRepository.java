package ru.vsu.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.vsu.hospital.model.dto.DoctorStatsDto;
import ru.vsu.hospital.model.entity.Doctor;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, String> {

    List<Doctor> findBySpecialization(String specialization);

    List<Doctor> findAllByOrderByLastNameAsc();

    @Query("SELECT new ru.vsu.hospital.model.dto.DoctorStatsDto(d.specialization, COUNT(d)) FROM Doctor d GROUP BY d.specialization ORDER BY COUNT(d) DESC")
    List<DoctorStatsDto> getStatsBySpecialization();
}
