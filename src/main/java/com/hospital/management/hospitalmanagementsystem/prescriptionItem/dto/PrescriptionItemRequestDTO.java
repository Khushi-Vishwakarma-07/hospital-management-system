package com.hospital.management.hospitalmanagementsystem.prescriptionItem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionItemRequestDTO {

    @NotBlank(message = "Medicine name is required")
    @Size(max = 200, message = "Medicine name must not exceed 200 characters")
    private String medicineName;

    @NotBlank(message = "Dosage is required")
    @Size(max = 100, message = "Dosage must not exceed 100 characters")
    private String dosage;

    @NotBlank(message = "Frequency is required")
    @Size(max = 100, message = "Frequency must not exceed 100 characters")
    private String frequency;

    @NotBlank(message = "Duration is required")
    @Size(max = 100, message = "Duration must not exceed 100 characters")
    private String duration;

    @Size(max = 100, message = "Route must not exceed 100 characters")
    private String route;

    @Size(max = 5000, message = "Instructions must not exceed 5000 characters")
    private String instructions;
}