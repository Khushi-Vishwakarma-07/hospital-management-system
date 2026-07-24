package com.hospital.management.hospitalmanagementsystem.prescriptionItem.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionItemResponseDTO {

    private Long id;
    private Long prescriptionId;

    private String medicineName;
    private String dosage;
    private String frequency;
    private String duration;
    private String route;
    private String instructions;
}