package com.hospital.management.hospitalmanagementsystem.prescription.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionRequestDTO {

    @NotNull(message = "Medical record id is required")
    @Positive(message = "Medical record id must be a positive number")
    private Long medicalRecordId;

    @Size(max = 5000, message = "Medication instructions must not exceed 5000 characters")
    private String medicationInstructions;

    @Size(max = 5000, message = "General instructions must not exceed 5000 characters")
    private String generalInstructions;
}