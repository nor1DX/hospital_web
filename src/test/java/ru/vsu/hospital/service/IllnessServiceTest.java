package ru.vsu.hospital.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vsu.hospital.model.dto.IllnessDto;
import ru.vsu.hospital.service.business.IllnessServiceImpl;
import ru.vsu.hospital.service.storage.IllnessStorageService;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IllnessServiceTest {

    @Mock
    private IllnessStorageService illnessStorageService;

    @InjectMocks
    private IllnessServiceImpl illnessService;

    @Test
    void getIllnessById() {
        IllnessDto illness = IllnessDto.builder()
                .id("1")
                .name("ОРВИ")
                .description("Острая респираторная вирусная инфекция")
                .severity("MILD")
                .build();

        when(illnessStorageService.getIllnessById("1")).thenReturn(illness);

        IllnessDto actual = illnessService.getIllnessById("1");

        System.out.println("=== getIllnessById ===");
        System.out.println("Запрос: id = 1");
        System.out.println("Болезнь: " + actual.getName() + ", тяжесть: " + actual.getSeverity());
        System.out.println("Описание: " + actual.getDescription());

        assertThat(actual).isEqualTo(illness);
    }

    @Test
    void getIllnesses() {
        List<IllnessDto> illnesses = List.of(
                IllnessDto.builder().id("1").name("ОРВИ").severity("MILD").build(),
                IllnessDto.builder().id("2").name("Пневмония").severity("SEVERE").build(),
                IllnessDto.builder().id("3").name("Гипертония").severity("MODERATE").build()
        );

        when(illnessStorageService.getIllnesses()).thenReturn(illnesses);

        List<IllnessDto> actual = illnessService.getIllnesses();

        System.out.println("=== getIllnesses ===");
        System.out.println("Всего болезней: " + actual.size());
        actual.forEach(i -> System.out.println("  - " + i.getName() + " [" + i.getSeverity() + "]"));

        assertThat(actual.size()).isEqualTo(3);
    }

    @Test
    void searchByDescription() {
        List<IllnessDto> results = List.of(
                IllnessDto.builder().id("1").name("ОРВИ").description("Острая респираторная вирусная инфекция").severity("MILD").build(),
                IllnessDto.builder().id("2").name("Пневмония").description("Воспаление лёгких. Серьёзное инфекционное заболевание нижних дыхательных путей").severity("SEVERE").build()
        );

        when(illnessStorageService.searchByDescription("дыхательных")).thenReturn(results);

        List<IllnessDto> actual = illnessService.searchByDescription("дыхательных");

        System.out.println("=== searchByDescription ===");
        System.out.println("Поиск по тексту: \"дыхательных\"");
        System.out.println("Найдено: " + actual.size());
        actual.forEach(i -> System.out.println("  - " + i.getName() + ": " + i.getDescription()));

        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.get(0).getName()).isEqualTo("ОРВИ");
        assertThat(actual.get(1).getName()).isEqualTo("Пневмония");
    }

    @Test
    void createIllness() {
        IllnessDto newIllness = IllnessDto.builder()
                .name("Диабет")
                .description("Хроническое заболевание, связанное с нарушением обмена веществ")
                .severity("MODERATE")
                .build();

        IllnessDto savedIllness = IllnessDto.builder()
                .id("4")
                .name("Диабет")
                .description("Хроническое заболевание, связанное с нарушением обмена веществ")
                .severity("MODERATE")
                .build();

        when(illnessStorageService.createIllness(newIllness)).thenReturn(savedIllness);

        IllnessDto actual = illnessService.createIllness(newIllness);

        System.out.println("=== createIllness ===");
        System.out.println("Создана болезнь: " + actual.getName());
        System.out.println("Присвоен id: " + actual.getId());
        System.out.println("Тяжесть: " + actual.getSeverity());

        assertThat(actual.getId()).isEqualTo("4");
        assertThat(actual.getName()).isEqualTo("Диабет");
    }
}
