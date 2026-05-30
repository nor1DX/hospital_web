package ru.vsu.hospital.service.storage;

import ru.vsu.hospital.model.dto.IllnessDto;

import java.util.List;

public interface IllnessStorageService {
    IllnessDto getIllnessById(String illnessId);
    List<IllnessDto> getIllnesses();
    boolean illnessExists(String illnessId);
    IllnessDto createIllness(IllnessDto illnessDto);
    IllnessDto updateIllness(IllnessDto illnessDto);
    IllnessDto deleteIllnessById(String illnessId);
    List<IllnessDto> searchByDescription(String searchText);
    long createIllnesses(String namePrefix, long startIndex, int count);
}
