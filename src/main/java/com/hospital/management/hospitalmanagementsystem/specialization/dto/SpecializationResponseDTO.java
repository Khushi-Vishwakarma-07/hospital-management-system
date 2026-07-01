package com.hospital.management.hospitalmanagementsystem.specialization.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecializationResponseDTO {

    private Long specializationId;
    private String specializationName;
    private String specializationDescription;
    private Long departmentId;
    private String departmentName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}