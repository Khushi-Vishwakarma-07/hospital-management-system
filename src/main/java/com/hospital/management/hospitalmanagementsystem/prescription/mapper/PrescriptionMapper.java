package com.hospital.management.hospitalmanagementsystem.prescription.mapper;

import com.hospital.management.hospitalmanagementsystem.medicalrecord.entity.MedicalRecord;
import com.hospital.management.hospitalmanagementsystem.prescription.dto.PrescriptionRequestDTO;
import com.hospital.management.hospitalmanagementsystem.prescription.dto.PrescriptionResponseDTO;
import com.hospital.management.hospitalmanagementsystem.prescription.dto.PrescriptionUpdateDTO;
import com.hospital.management.hospitalmanagementsystem.prescription.entity.Prescription;

public final class PrescriptionMapper {

    private PrescriptionMapper() {}

    public static Prescription toEntity(PrescriptionRequestDTO dto, MedicalRecord medicalRecord) {
        return Prescription.builder()
                .medicalRecord(medicalRecord)
                .medicationInstructions(dto.getMedicationInstructions())
                .generalInstructions(dto.getGeneralInstructions())
                .build();
    }

    public static PrescriptionResponseDTO toDTO(Prescription prescription) {
        return PrescriptionResponseDTO.builder()
                .id(prescription.getId())
                .medicalRecordId(prescription.getMedicalRecord().getId())
                .medicationInstructions(prescription.getMedicationInstructions())
                .generalInstructions(prescription.getGeneralInstructions())
                .createdAt(prescription.getCreatedAt())
                .updatedAt(prescription.getUpdatedAt())
                .build();
    }

    /** Mutates the existing entity in place — avoids detaching and re-persisting on update. */
    public static void updateEntity(Prescription prescription, PrescriptionUpdateDTO dto) {
        prescription.setMedicationInstructions(dto.getMedicationInstructions());
        prescription.setGeneralInstructions(dto.getGeneralInstructions());
    }
}