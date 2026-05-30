package ru.vsu.hospital.component.mapper;

import org.mapstruct.Mapper;
import ru.vsu.hospital.model.dto.IllnessDto;
import ru.vsu.hospital.model.entity.Illness;

@Mapper(componentModel = "spring")
public interface IllnessMapper {
    IllnessDto toDto(Illness illness);
    Illness toEntity(IllnessDto illnessDto);
}
