package com.hospital.management.hospitalmanagementsystem.doctor.dto;

import com.hospital.management.hospitalmanagementsystem.doctor.enums.DoctorSpecialization;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorResponseDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String fullName;

    private DoctorSpecialization specialization;

    private String phoneNumber;

    private String email;

    private Integer experienceYears;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}