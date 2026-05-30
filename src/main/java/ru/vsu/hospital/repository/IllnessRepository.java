package ru.vsu.hospital.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import ru.vsu.hospital.model.entity.Illness;

import java.util.List;

public interface IllnessRepository extends MongoRepository<Illness, String> {

    @Query("{ $text: { $search: ?0 } }")
    List<Illness> findByDescriptionText(String searchText);
}
