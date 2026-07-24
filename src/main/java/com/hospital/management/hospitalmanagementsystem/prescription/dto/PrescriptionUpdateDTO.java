package com.hospital.management.hospitalmanagementsystem.prescription.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionUpdateDTO {

    @Size(max = 5000, message = "Medication instructions must not exceed 5000 characters")
    private String medicationInstructions;

    @Size(max = 5000, message = "General instructions must not exceed 5000 characters")
    private String generalInstructions;
}