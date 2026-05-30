package ru.vsu.hospital.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vsu.hospital.model.dto.DoctorDto;
import ru.vsu.hospital.service.business.DoctorServiceImpl;
import ru.vsu.hospital.service.storage.DoctorStorageService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock
    private DoctorStorageService doctorStorageService;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    @Test
    void getDoctorById() {
        DoctorDto doctor = DoctorDto.builder()
                .id("1")
                .firstName("Иван")
                .lastName("Петров")
                .specialization("Терапевт")
                .build();

        when(doctorStorageService.getDoctorById("1")).thenReturn(doctor);

        DoctorDto actual = doctorService.getDoctorById("1");

        System.out.println("=== getDoctorById ===");
        System.out.println("Запрос: id = 1");
        System.out.println("Результат: " + actual.getFirstName() + " " + actual.getLastName() + ", специализация: " + actual.getSpecialization());

        assertThat(actual).isEqualTo(DoctorDto.builder()
                .id("1").firstName("Иван").lastName("Петров").specialization("Терапевт").build());
    }

    @Test
    void getDoctors() {
        List<DoctorDto> doctors = List.of(
                DoctorDto.builder().id("1").firstName("Иван").lastName("Петров").specialization("Терапевт").build(),
                DoctorDto.builder().id("2").firstName("Мария").lastName("Сидорова").specialization("Кардиолог").build()
        );

        when(doctorStorageService.getDoctors()).thenReturn(doctors);

        List<DoctorDto> actual = doctorService.getDoctors();

        System.out.println("=== getDoctors ===");
        System.out.println("Всего врачей: " + actual.size());
        actual.forEach(d -> System.out.println("  - " + d.getFirstName() + " " + d.getLastName() + " (" + d.getSpecialization() + ")"));

        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createDoctor() {
        DoctorDto newDoctor = DoctorDto.builder()
                .firstName("Алексей")
                .lastName("Козлов")
                .specialization("Хирург")
                .build();

        DoctorDto savedDoctor = DoctorDto.builder()
                .id("3")
                .firstName("Алексей")
                .lastName("Козлов")
                .specialization("Хирург")
                .build();

        when(doctorStorageService.createDoctor(newDoctor)).thenReturn(savedDoctor);

        DoctorDto actual = doctorService.createDoctor(newDoctor);

        System.out.println("=== createDoctor ===");
        System.out.println("Создан врач: " + actual.getFirstName() + " " + actual.getLastName());
        System.out.println("Присвоен id: " + actual.getId());
        System.out.println("Специализация: " + actual.getSpecialization());

        assertThat(actual.getId()).isEqualTo("3");
        assertThat(actual.getSpecialization()).isEqualTo("Хирург");
    }

    @Test
    void deleteDoctor() {
        DoctorDto doctor = DoctorDto.builder()
                .id("1")
                .firstName("Иван")
                .lastName("Петров")
                .specialization("Терапевт")
                .build();

        when(doctorStorageService.deleteDoctorById("1")).thenReturn(doctor);

        DoctorDto actual = doctorService.deleteDoctorById("1");

        System.out.println("=== deleteDoctor ===");
        System.out.println("Удалён врач: " + actual.getFirstName() + " " + actual.getLastName() + " (id=" + actual.getId() + ")");

        assertThat(actual.getId()).isEqualTo("1");
    }
}
