package ru.vsu.hospital.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vsu.hospital.model.dto.DoctorDto;
import ru.vsu.hospital.model.dto.DoctorStatsDto;
import ru.vsu.hospital.service.business.DoctorServiceImpl;
import ru.vsu.hospital.service.kafka.KafkaEventProducer;
import ru.vsu.hospital.service.storage.DoctorStorageService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock
    private DoctorStorageService doctorStorageService;

    @Mock
    private KafkaEventProducer kafkaEventProducer;

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

    @Test
    void getDoctorsBySpecialization() {
        List<DoctorDto> therapists = List.of(
                DoctorDto.builder().id("1").firstName("Иван").lastName("Петров").specialization("Терапевт").build(),
                DoctorDto.builder().id("2").firstName("Анна").lastName("Кузнецова").specialization("Терапевт").build()
        );

        when(doctorStorageService.getDoctorsBySpecialization("Терапевт")).thenReturn(therapists);

        List<DoctorDto> actual = doctorService.getDoctorsBySpecialization("Терапевт");

        System.out.println("=== getDoctorsBySpecialization ===");
        System.out.println("Фильтр: специализация = Терапевт");
        System.out.println("Найдено врачей: " + actual.size());
        actual.forEach(d -> System.out.println("  - " + d.getFirstName() + " " + d.getLastName()));

        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.get(0).getSpecialization()).isEqualTo("Терапевт");
    }

    @Test
    void getDoctorsSortedByLastName() {
        List<DoctorDto> sorted = List.of(
                DoctorDto.builder().id("2").firstName("Анна").lastName("Кузнецова").specialization("Терапевт").build(),
                DoctorDto.builder().id("1").firstName("Иван").lastName("Петров").specialization("Терапевт").build(),
                DoctorDto.builder().id("3").firstName("Мария").lastName("Сидорова").specialization("Кардиолог").build()
        );

        when(doctorStorageService.getDoctorsSortedByLastName()).thenReturn(sorted);

        List<DoctorDto> actual = doctorService.getDoctorsSortedByLastName();

        System.out.println("=== getDoctorsSortedByLastName ===");
        System.out.println("Сортировка А-Я по фамилии:");
        actual.forEach(d -> System.out.println("  - " + d.getLastName() + " " + d.getFirstName()));

        assertThat(actual.size()).isEqualTo(3);
        assertThat(actual.get(0).getLastName()).isEqualTo("Кузнецова");
        assertThat(actual.get(2).getLastName()).isEqualTo("Сидорова");
    }

    @Test
    void getDoctorStats() {
        List<DoctorStatsDto> stats = List.of(
                new DoctorStatsDto("Терапевт", 5L),
                new DoctorStatsDto("Кардиолог", 3L),
                new DoctorStatsDto("Хирург", 2L)
        );

        when(doctorStorageService.getDoctorStats()).thenReturn(stats);

        List<DoctorStatsDto> actual = doctorService.getDoctorStats();

        System.out.println("=== getDoctorStats ===");
        System.out.println("Статистика по специализациям:");
        actual.forEach(s -> System.out.println("  - " + s.getSpecialization() + ": " + s.getCount() + " врачей"));

        assertThat(actual.size()).isEqualTo(3);
        assertThat(actual.get(0).getSpecialization()).isEqualTo("Терапевт");
        assertThat(actual.get(0).getCount()).isEqualTo(5L);
    }
}
