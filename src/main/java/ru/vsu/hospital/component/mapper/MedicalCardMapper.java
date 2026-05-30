package ru.vsu.hospital.component.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.vsu.hospital.model.dto.MedicalCardDto;
import ru.vsu.hospital.model.entity.MedicalCard;
import ru.vsu.hospital.model.request.CreateMedicalCardRequest;

@Mapper(componentModel = "spring")
public interface MedicalCardMapper {
    MedicalCardDto toDto(MedicalCard medicalCard);
    MedicalCard toEntity(MedicalCardDto medicalCardDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctorId", ignore = true)
    @Mapping(target = "treatmentStart", ignore = true)
    @Mapping(target = "expireAt", ignore = true)
    MedicalCard toEntity(CreateMedicalCardRequest request);
}
