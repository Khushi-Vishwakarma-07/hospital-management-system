package com.hospital.management.hospitalmanagementsystem.prescription.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionResponseDTO {

    private Long id;

    private Long medicalRecordId;

    private String medicationInstructions;
    private String generalInstructions;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}