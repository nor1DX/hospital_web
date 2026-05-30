package ru.vsu.hospital.service.storage;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vsu.hospital.component.mapper.IllnessMapper;
import ru.vsu.hospital.model.dto.IllnessDto;
import ru.vsu.hospital.model.entity.Illness;
import ru.vsu.hospital.repository.IllnessRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class IllnessStorageServiceImpl implements IllnessStorageService {
    private final IllnessRepository illnessRepository;
    private final IllnessMapper illnessMapper;

    @Override
    public IllnessDto getIllnessById(String illnessId) {
        return illnessMapper.toDto(
                illnessRepository.findById(illnessId)
                        .orElseThrow(() -> new IllegalArgumentException("Illness does not exist"))
        );
    }

    @Override
    public List<IllnessDto> getIllnesses() {
        return illnessRepository.findAll()
                .stream()
                .map(illnessMapper::toDto)
                .toList();
    }

    @Override
    public boolean illnessExists(String illnessId) {
        return illnessRepository.existsById(illnessId);
    }

    @Override
    public IllnessDto createIllness(IllnessDto illnessDto) {
        Illness illness = illnessMapper.toEntity(illnessDto);
        return illnessMapper.toDto(illnessRepository.save(illness));
    }

    @Override
    public IllnessDto updateIllness(IllnessDto illnessDto) {
        Illness illness = illnessRepository.findById(illnessDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Illness does not exist"));

        illness.setName(illnessDto.getName());
        illness.setDescription(illnessDto.getDescription());
        illness.setSeverity(illnessDto.getSeverity());

        return illnessMapper.toDto(illnessRepository.save(illness));
    }

    @Override
    public IllnessDto deleteIllnessById(String illnessId) {
        IllnessDto deleted = getIllnessById(illnessId);
        illnessRepository.deleteById(illnessId);
        return deleted;
    }

    @Override
    public List<IllnessDto> searchByDescription(String searchText) {
        return illnessRepository.findByDescriptionText(searchText)
                .stream()
                .map(illnessMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public long createIllnesses(String namePrefix, long startIndex, int count) {
        String prefix = (namePrefix == null || namePrefix.isBlank()) ? "load-illness" : namePrefix;
        List<Illness> illnesses = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            illnesses.add(Illness.builder()
                    .name(prefix + "-" + (startIndex + i))
                    .description("Автоматически сгенерированное заболевание номер " + (startIndex + i))
                    .severity("MILD")
                    .build());
        }
        return illnessRepository.saveAll(illnesses).size();
    }
}
