package ru.vsu.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.vsu.hospital.model.entity.Illness;

import java.util.List;

public interface IllnessRepository extends JpaRepository<Illness, String> {

    @Query("SELECT i FROM Illness i WHERE LOWER(i.description) LIKE LOWER(CONCAT('%', :text, '%'))")
    List<Illness> findByDescriptionText(String text);
}
