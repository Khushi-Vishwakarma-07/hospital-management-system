package com.hospital.management.hospitalmanagementsystem.prescription.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionUpdateDTO {

    private String medicationInstructions;

    private String generalInstructions;
}