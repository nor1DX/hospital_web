package ru.vsu.hospital.component.mapper;

import org.mapstruct.Mapper;
import ru.vsu.hospital.model.dto.DoctorDto;
import ru.vsu.hospital.model.entity.Doctor;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    DoctorDto toDto(Doctor doctor);
    Doctor toEntity(DoctorDto doctorDto);
}
