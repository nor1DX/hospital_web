package ru.vsu.hospital.service.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vsu.hospital.config.KafkaConfig;
import ru.vsu.hospital.model.dto.DoctorDto;
import ru.vsu.hospital.model.dto.DoctorStatsDto;
import ru.vsu.hospital.service.kafka.KafkaEventProducer;
import ru.vsu.hospital.service.storage.DoctorStorageService;

import java.util.List;

@Service
@AllArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorStorageService doctorStorageService;
    private final KafkaEventProducer kafkaEventProducer;

    @Override
    public DoctorDto getDoctorById(String doctorId) {
        return doctorStorageService.getDoctorById(doctorId);
    }

    @Override
    public List<DoctorDto> getDoctors() {
        return doctorStorageService.getDoctors();
    }

    @Override
    public List<DoctorDto> getDoctorsBySpecialization(String specialization) {
        return doctorStorageService.getDoctorsBySpecialization(specialization);
    }

    @Override
    public List<DoctorDto> getDoctorsSortedByLastName() {
        return doctorStorageService.getDoctorsSortedByLastName();
    }

    @Override
    public List<DoctorStatsDto> getDoctorStats() {
        return doctorStorageService.getDoctorStats();
    }

    @Override
    public DoctorDto createDoctor(DoctorDto doctorDto) {
        DoctorDto created = doctorStorageService.createDoctor(doctorDto);
        kafkaEventProducer.send(KafkaConfig.DOCTORS_TOPIC, "CREATED", "DOCTOR", created.getId(), created, 1);
        return created;
    }

    @Override
    public DoctorDto updateDoctor(DoctorDto doctorDto) {
        DoctorDto updated = doctorStorageService.updateDoctor(doctorDto);
        kafkaEventProducer.send(KafkaConfig.DOCTORS_TOPIC, "UPDATED", "DOCTOR", updated.getId(), updated, 1);
        return updated;
    }

    @Override
    public DoctorDto deleteDoctorById(String doctorId) {
        DoctorDto deleted = doctorStorageService.deleteDoctorById(doctorId);
        kafkaEventProducer.send(KafkaConfig.DOCTORS_TOPIC, "DELETED", "DOCTOR", doctorId, deleted, 1);
        return deleted;
    }

    @Override
    public long createDoctors(String namePrefix, long startIndex, int count) {
        return doctorStorageService.createDoctors(namePrefix, startIndex, count);
    }
}
