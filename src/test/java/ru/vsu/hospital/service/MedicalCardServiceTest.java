package ru.vsu.hospital.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vsu.hospital.model.dto.MedicalCardDto;
import ru.vsu.hospital.model.request.CreateMedicalCardRequest;
import ru.vsu.hospital.service.business.MedicalCardServiceImpl;
import ru.vsu.hospital.service.storage.MedicalCardStorageService;

import com.mongodb.client.result.UpdateResult;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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

        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.get(0).getDiagnosis()).isEqualTo("ОРВИ средней тяжести");
        assertThat(actual.get(1).getDiagnosis()).isEqualTo("Гипертония I степени");
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

        assertThat(actual.getId()).isEqualTo("1");
        assertThat(actual.getPatientId()).isEqualTo("p1");
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

        when(medicalCardStorageService.addDoctor("1", "d1")).thenReturn(mock(UpdateResult.class));
        when(medicalCardStorageService.getMedicalCardById("1")).thenReturn(cardAfterUpdate);

        MedicalCardDto actual = medicalCardService.addDoctor("1", "d1");

        assertThat(actual.getDoctorId()).isEqualTo("d1");
        assertThat(actual.getId()).isEqualTo("1");
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

        when(medicalCardStorageService.markAsRecovered("1")).thenReturn(mock(UpdateResult.class));
        when(medicalCardStorageService.getMedicalCardById("1")).thenReturn(recoveredCard);

        MedicalCardDto actual = medicalCardService.markAsRecovered("1");

        // expireAt не null — пациент отмечен как выздоровевший, карточка будет удалена TTL-индексом
        assertThat(actual.getExpireAt()).isNotNull();
        assertThat(actual.getId()).isEqualTo("1");
    }
}
