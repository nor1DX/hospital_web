package ru.vsu.hospital.component.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.vsu.hospital.model.dto.PatientDto;
import ru.vsu.hospital.model.entity.Patient;
import ru.vsu.hospital.model.request.CreatePatientRequest;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    PatientDto toDto(Patient patient);
    Patient toEntity(PatientDto patientDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "medicalCardId", ignore = true)
    Patient toEntity(CreatePatientRequest request);
}
