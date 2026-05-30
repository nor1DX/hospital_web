package ru.vsu.hospital.service.storage;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vsu.hospital.component.mapper.DoctorMapper;
import ru.vsu.hospital.model.dto.DoctorDto;
import ru.vsu.hospital.model.entity.Doctor;
import ru.vsu.hospital.repository.DoctorRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class DoctorStorageServiceImpl implements DoctorStorageService {
    private final MedicalCardStorageService medicalCardStorageService;
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    @Override
    public DoctorDto getDoctorById(String doctorId) {
        return doctorMapper.toDto(
                doctorRepository.findById(doctorId)
                        .orElseThrow(() -> new IllegalArgumentException("Doctor does not exist"))
        );
    }

    @Override
    public List<DoctorDto> getDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(doctorMapper::toDto)
                .toList();
    }

    @Override
    public DoctorDto createDoctor(DoctorDto doctorDto) {
        Doctor doctor = doctorMapper.toEntity(doctorDto);
        return doctorMapper.toDto(doctorRepository.save(doctor));
    }

    @Override
    public DoctorDto updateDoctor(DoctorDto doctorDto) {
        Doctor doctor = doctorRepository.findById(doctorDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor does not exist"));

        doctor.setFirstName(doctorDto.getFirstName());
        doctor.setLastName(doctorDto.getLastName());
        doctor.setSpecialization(doctorDto.getSpecialization());

        return doctorMapper.toDto(doctorRepository.save(doctor));
    }

    @Override
    @Transactional
    public DoctorDto deleteDoctorById(String doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor does not exist"));

        medicalCardStorageService.removeDoctor(doctor.getId());

        doctorRepository.deleteById(doctorId);

        return doctorMapper.toDto(doctor);
    }

    @Override
    public boolean doctorExists(String doctorId) {
        return doctorRepository.existsById(doctorId);
    }
}
