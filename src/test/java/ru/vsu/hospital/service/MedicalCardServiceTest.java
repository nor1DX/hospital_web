package ru.vsu.hospital.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vsu.hospital.model.dto.MedicalCardDetailsDto;
import ru.vsu.hospital.model.dto.MedicalCardDto;
import ru.vsu.hospital.model.request.CreateMedicalCardRequest;
import ru.vsu.hospital.service.business.MedicalCardServiceImpl;
import ru.vsu.hospital.service.storage.MedicalCardStorageService;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MedicalCardServiceTest {

    @Mock
    private MedicalCardStorageService medicalCardStorageService;

    @InjectMocks
    private MedicalCardServiceImpl medicalCardService;

    @Test
    void getMedicalCardById() {
        MedicalCardDto card = MedicalCardDto.builder()
                .id("1")
                .patientId("p1")
                .doctorId("d1")
                .illnessId("i1")
                .diagnosis("ОРВИ средней тяжести")
                .expireAt(null)
                .build();

        when(medicalCardStorageService.getMedicalCardById("1")).thenReturn(card);

        MedicalCardDto actual = medicalCardService.getMedicalCardById("1");

        System.out.println("=== getMedicalCardById ===");
        System.out.println("Карта: id=" + actual.getId() + ", диагноз: " + actual.getDiagnosis());
        System.out.println("Пациент выздоровел: " + (actual.getExpireAt() != null ? "да" : "нет"));

        assertThat(actual.getId()).isEqualTo("1");
        assertThat(actual.getDiagnosis()).isEqualTo("ОРВИ средней тяжести");
        assertThat(actual.getExpireAt()).isNull();
    }

    @Test
    void getMedicalCards() {
        List<MedicalCardDto> cards = List.of(
                MedicalCardDto.builder().id("1").patientId("p1").doctorId("d1").illnessId("i1").diagnosis("ОРВИ средней тяжести").build(),
                MedicalCardDto.builder().id("2").patientId("p2").doctorId("d2").illnessId("i3").diagnosis("Гипертония I степени").build()
        );

        when(medicalCardStorageService.getMedicalCards()).thenReturn(cards);

        List<MedicalCardDto> actual = medicalCardService.getMedicalCards();

        System.out.println("=== getMedicalCards ===");
        System.out.println("Всего карт: " + actual.size());
        actual.forEach(c -> System.out.println("  - " + c.getDiagnosis() + " (пациент: " + c.getPatientId() + ")"));

        assertThat(actual.size()).isEqualTo(2);
    }

    @Test
    void createMedicalCard() {
        CreateMedicalCardRequest request = CreateMedicalCardRequest.builder()
                .patientId("p1")
                .illnessId("i1")
                .diagnosis("ОРВИ средней тяжести")
                .build();

        MedicalCardDto savedCard = MedicalCardDto.builder()
                .id("1")
                .patientId("p1")
                .illnessId("i1")
                .diagnosis("ОРВИ средней тяжести")
                .expireAt(null)
                .build();

        when(medicalCardStorageService.createMedicalCard(request)).thenReturn(savedCard);

        MedicalCardDto actual = medicalCardService.createMedicalCard(request);

        System.out.println("=== createMedicalCard ===");
        System.out.println("Создана карта: id=" + actual.getId());
        System.out.println("Пациент: " + actual.getPatientId() + ", диагноз: " + actual.getDiagnosis());

        assertThat(actual.getId()).isEqualTo("1");
        assertThat(actual.getExpireAt()).isNull();
    }

    @Test
    void addDoctor() {
        MedicalCardDto cardAfterUpdate = MedicalCardDto.builder()
                .id("1")
                .patientId("p1")
                .doctorId("d1")
                .illnessId("i1")
                .diagnosis("ОРВИ средней тяжести")
                .build();

        doNothing().when(medicalCardStorageService).addDoctor("1", "d1");
        when(medicalCardStorageService.getMedicalCardById("1")).thenReturn(cardAfterUpdate);

        MedicalCardDto actual = medicalCardService.addDoctor("1", "d1");

        System.out.println("=== addDoctor ===");
        System.out.println("Карта id=" + actual.getId() + ", назначен врач: " + actual.getDoctorId());

        assertThat(actual.getDoctorId()).isEqualTo("d1");
    }

    @Test
    void getMedicalCardsWithDetails() {
        MedicalCardDetailsDto detail1 = mock(MedicalCardDetailsDto.class);
        when(detail1.getId()).thenReturn("1");
        when(detail1.getDiagnosis()).thenReturn("ОРВИ средней тяжести");
        when(detail1.getDoctorName()).thenReturn("Иван Петров");
        when(detail1.getPatientName()).thenReturn("Дмитрий Смирнов");
        when(detail1.getIllnessName()).thenReturn("ОРВИ");

        MedicalCardDetailsDto detail2 = mock(MedicalCardDetailsDto.class);
        when(detail2.getId()).thenReturn("2");
        when(detail2.getDiagnosis()).thenReturn("Гипертония I степени");
        when(detail2.getDoctorName()).thenReturn("Не назначен");
        when(detail2.getPatientName()).thenReturn("Елена Иванова");
        when(detail2.getIllnessName()).thenReturn("Гипертония");

        when(medicalCardStorageService.getMedicalCardsWithDetails()).thenReturn(List.of(detail1, detail2));

        List<MedicalCardDetailsDto> actual = medicalCardService.getMedicalCardsWithDetails();

        System.out.println("=== getMedicalCardsWithDetails ===");
        System.out.println("JOIN: медкарты + врач + пациент + болезнь");
        actual.forEach(c -> System.out.println(
                "  Карта id=" + c.getId()
                + " | Пациент: " + c.getPatientName()
                + " | Врач: " + c.getDoctorName()
                + " | Болезнь: " + c.getIllnessName()
                + " | Диагноз: " + c.getDiagnosis()
        ));

        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.get(0).getDoctorName()).isEqualTo("Иван Петров");
        assertThat(actual.get(1).getDoctorName()).isEqualTo("Не назначен");
    }

    @Test
    void markAsRecovered() {
        Date recoveredAt = new Date();

        MedicalCardDto recoveredCard = MedicalCardDto.builder()
                .id("1")
                .patientId("p1")
                .doctorId("d1")
                .illnessId("i1")
                .diagnosis("ОРВИ средней тяжести")
                .expireAt(recoveredAt)
                .build();

        doNothing().when(medicalCardStorageService).markAsRecovered("1");
        when(medicalCardStorageService.getMedicalCardById("1")).thenReturn(recoveredCard);

        MedicalCardDto actual = medicalCardService.markAsRecovered("1");

        System.out.println("=== markAsRecovered ===");
        System.out.println("Пациент выздоровел, карта id=" + actual.getId());
        System.out.println("expireAt установлен: " + actual.getExpireAt());

        assertThat(actual.getExpireAt()).isNotNull();
    }
}
