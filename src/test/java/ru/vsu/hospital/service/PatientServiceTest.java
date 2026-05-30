package ru.vsu.hospital.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vsu.hospital.model.dto.PatientDto;
import ru.vsu.hospital.model.request.CreatePatientRequest;
import ru.vsu.hospital.service.business.PatientServiceImpl;
import ru.vsu.hospital.service.kafka.KafkaEventProducer;
import ru.vsu.hospital.service.storage.PatientStorageService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientStorageService patientStorageService;

    @Mock
    private KafkaEventProducer kafkaEventProducer;

    @InjectMocks
    private PatientServiceImpl patientService;

    @Test
    void getPatientById() {
        PatientDto patient = PatientDto.builder()
                .id("1")
                .firstName("Дмитрий")
                .lastName("Смирнов")
                .dateOfBirth("1985-03-15")
                .medicalCardId(null)
                .build();

        when(patientStorageService.getPatientById("1")).thenReturn(patient);

        PatientDto actual = patientService.getPatientById("1");

        System.out.println("=== getPatientById ===");
        System.out.println("Запрос: id = 1");
        System.out.println("Результат: " + actual.getFirstName() + " " + actual.getLastName() + ", дата рождения: " + actual.getDateOfBirth());
        System.out.println("Медицинская карта: " + (actual.getMedicalCardId() == null ? "не назначена" : actual.getMedicalCardId()));

        assertThat(actual).isEqualTo(patient);
    }

    @Test
    void getPatients() {
        List<PatientDto> patients = List.of(
                PatientDto.builder().id("1").firstName("Дмитрий").lastName("Смирнов").dateOfBirth("1985-03-15").build(),
                PatientDto.builder().id("2").firstName("Елена").lastName("Новикова").dateOfBirth("1990-07-22").build()
        );

        when(patientStorageService.getPatients()).thenReturn(patients);

        List<PatientDto> actual = patientService.getPatients();

        System.out.println("=== getPatients ===");
        System.out.println("Всего пациентов: " + actual.size());
        actual.forEach(p -> System.out.println("  - " + p.getFirstName() + " " + p.getLastName() + ", д.р. " + p.getDateOfBirth()));

        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createPatient() {
        CreatePatientRequest request = CreatePatientRequest.builder()
                .firstName("Сергей")
                .lastName("Федоров")
                .dateOfBirth("1978-11-08")
                .build();

        PatientDto savedPatient = PatientDto.builder()
                .id("3")
                .firstName("Сергей")
                .lastName("Федоров")
                .dateOfBirth("1978-11-08")
                .medicalCardId(null)
                .build();

        when(patientStorageService.createPatient(request)).thenReturn(savedPatient);

        PatientDto actual = patientService.createPatient(request);

        System.out.println("=== createPatient ===");
        System.out.println("Создан пациент: " + actual.getFirstName() + " " + actual.getLastName());
        System.out.println("Присвоен id: " + actual.getId());
        System.out.println("Медицинская карта: " + (actual.getMedicalCardId() == null ? "не назначена" : actual.getMedicalCardId()));

        assertThat(actual.getId()).isEqualTo("3");
        assertThat(actual.getMedicalCardId()).isNull();
    }

    @Test
    void deletePatient() {
        PatientDto patient = PatientDto.builder()
                .id("1")
                .firstName("Дмитрий")
                .lastName("Смирнов")
                .dateOfBirth("1985-03-15")
                .build();

        when(patientStorageService.deletePatient("1")).thenReturn(patient);

        PatientDto actual = patientService.deletePatient("1");

        System.out.println("=== deletePatient ===");
        System.out.println("Удалён пациент: " + actual.getFirstName() + " " + actual.getLastName() + " (id=" + actual.getId() + ")");

        assertThat(actual.getId()).isEqualTo("1");
    }
}
