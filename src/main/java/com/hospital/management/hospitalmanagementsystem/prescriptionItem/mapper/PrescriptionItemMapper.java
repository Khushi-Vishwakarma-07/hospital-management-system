package com.hospital.management.hospitalmanagementsystem.prescriptionItem.mapper;

import com.hospital.management.hospitalmanagementsystem.prescription.entity.Prescription;
import com.hospital.management.hospitalmanagementsystem.prescriptionItem.dto.PrescriptionItemRequestDTO;
import com.hospital.management.hospitalmanagementsystem.prescriptionItem.dto.PrescriptionItemResponseDTO;
import com.hospital.management.hospitalmanagementsystem.prescriptionItem.entity.PrescriptionItem;

public final class PrescriptionItemMapper {

    private PrescriptionItemMapper() {}

    public static PrescriptionItem toEntity(PrescriptionItemRequestDTO dto, Prescription prescription) {
        return PrescriptionItem.builder()
                .prescription(prescription)
                .medicineName(dto.getMedicineName())
                .dosage(dto.getDosage())
                .frequency(dto.getFrequency())
                .duration(dto.getDuration())
                .route(dto.getRoute())
                .instructions(dto.getInstructions())
                .build();
    }

    public static PrescriptionItemResponseDTO toDTO(PrescriptionItem item) {
        return PrescriptionItemResponseDTO.builder()
                .id(item.getId())
                .prescriptionId(item.getPrescription().getId())
                .medicineName(item.getMedicineName())
                .dosage(item.getDosage())
                .frequency(item.getFrequency())
                .duration(item.getDuration())
                .route(item.getRoute())
                .instructions(item.getInstructions())
                .build();
    }

    /** Mutates the existing entity in place — avoids detaching and re-persisting on update. */
    public static void updateEntity(PrescriptionItem item, PrescriptionItemRequestDTO dto) {
        item.setMedicineName(dto.getMedicineName());
        item.setDosage(dto.getDosage());
        item.setFrequency(dto.getFrequency());
        item.setDuration(dto.getDuration());
        item.setRoute(dto.getRoute());
        item.setInstructions(dto.getInstructions());
    }
}