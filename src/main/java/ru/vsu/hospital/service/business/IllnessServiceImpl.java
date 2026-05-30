package ru.vsu.hospital.service.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vsu.hospital.model.dto.IllnessDto;
import ru.vsu.hospital.service.storage.IllnessStorageService;

import java.util.List;

@Service
@AllArgsConstructor
public class IllnessServiceImpl implements IllnessService {
    private final IllnessStorageService illnessStorageService;

    @Override
    public IllnessDto getIllnessById(String illnessId) {
        return illnessStorageService.getIllnessById(illnessId);
    }

    @Override
    public List<IllnessDto> getIllnesses() {
        return illnessStorageService.getIllnesses();
    }

    @Override
    public IllnessDto createIllness(IllnessDto illnessDto) {
        return illnessStorageService.createIllness(illnessDto);
    }

    @Override
    public IllnessDto updateIllness(IllnessDto illnessDto) {
        return illnessStorageService.updateIllness(illnessDto);
    }

    @Override
    public IllnessDto deleteIllnessById(String illnessId) {
        return illnessStorageService.deleteIllnessById(illnessId);
    }

    @Override
    public List<IllnessDto> searchByDescription(String searchText) {
        return illnessStorageService.searchByDescription(searchText);
    }

    @Override
    public long createIllnesses(String namePrefix, long startIndex, int count) {
        return illnessStorageService.createIllnesses(namePrefix, startIndex, count);
    }
}
