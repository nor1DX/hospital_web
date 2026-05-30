package ru.vsu.hospital.service.business;

import ru.vsu.hospital.model.dto.IllnessDto;

import java.util.List;

public interface IllnessService {
    IllnessDto getIllnessById(String illnessId);
    List<IllnessDto> getIllnesses();
    IllnessDto createIllness(IllnessDto illnessDto);
    IllnessDto updateIllness(IllnessDto illnessDto);
    IllnessDto deleteIllnessById(String illnessId);
    List<IllnessDto> searchByDescription(String searchText);
}
