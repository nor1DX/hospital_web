package ru.vsu.hospital.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.vsu.hospital.model.dto.MedicalCardDto;
import ru.vsu.hospital.model.request.CreateMedicalCardRequest;
import ru.vsu.hospital.model.request.UpdateMedicalCardDoctorRequest;
import ru.vsu.hospital.model.request.UpdateMedicalCardIllnessRequest;
import ru.vsu.hospital.service.business.MedicalCardService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/medical-cards")
public class MedicalCardController {
    private final MedicalCardService medicalCardService;

    @GetMapping("/{medicalCardId}")
    public MedicalCardDto getMedicalCard(@PathVariable String medicalCardId) {
        return medicalCardService.getMedicalCardById(medicalCardId);
    }

    @GetMapping
    public List<MedicalCardDto> getMedicalCards() {
        return medicalCardService.getMedicalCards();
    }

    @PostMapping
    public MedicalCardDto createMedicalCard(@RequestBody CreateMedicalCardRequest request) {
        return medicalCardService.createMedicalCard(request);
    }

    @PutMapping
    public MedicalCardDto updateMedicalCard(@RequestBody MedicalCardDto medicalCardDto) {
        return medicalCardService.updateMedicalCard(medicalCardDto);
    }

    @DeleteMapping("/{medicalCardId}")
    public MedicalCardDto deleteMedicalCard(@PathVariable String medicalCardId) {
        return medicalCardService.deleteMedicalCardById(medicalCardId);
    }

    @PostMapping("/{medicalCardId}/doctors")
    public MedicalCardDto addDoctor(@PathVariable String medicalCardId,
                                    @RequestBody UpdateMedicalCardDoctorRequest request) {
        return medicalCardService.addDoctor(medicalCardId, request.getDoctorId());
    }

    @DeleteMapping("/{medicalCardId}/doctors")
    public MedicalCardDto removeDoctor(@PathVariable String medicalCardId,
                                       @RequestBody UpdateMedicalCardDoctorRequest request) {
        return medicalCardService.removeDoctor(medicalCardId, request.getDoctorId());
    }

    @PostMapping("/{medicalCardId}/illnesses")
    public MedicalCardDto addIllness(@PathVariable String medicalCardId,
                                     @RequestBody UpdateMedicalCardIllnessRequest request) {
        return medicalCardService.addIllness(medicalCardId, request.getIllnessId());
    }

    @PostMapping("/{medicalCardId}/recover")
    public MedicalCardDto markAsRecovered(@PathVariable String medicalCardId) {
        return medicalCardService.markAsRecovered(medicalCardId);
    }
}
