package com.hospital.management.hospitalmanagementsystem.specialization.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecializationRequestDTO {

    @NotBlank(message = "Specialization name is required")
    @Size(min = 2, max = 100, message = "Specialization name must be between 2 and 100 characters")
    private String specializationName;

    @Size(max = 500, message = "Specialization description must not exceed 500 characters")
    private String specializationDescription;

    @NotNull(message = "Department id is required")
    @Positive(message = "Department id must be positive")
    private Long departmentId;
}