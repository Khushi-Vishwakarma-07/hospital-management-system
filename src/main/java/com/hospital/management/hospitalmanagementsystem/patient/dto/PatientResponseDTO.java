package com.hospital.management.hospitalmanagementsystem.patient.dto;

import com.hospital.management.hospitalmanagementsystem.patient.enums.BloodGroup;
import com.hospital.management.hospitalmanagementsystem.patient.enums.Gender;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientResponseDTO {

    private Long id;

    private String firstName;
    private String lastName;

    private String fullName;
    private Integer age;

    private LocalDate dateOfBirth;

    private Gender gender;

    private String phone;
    private String email;
    private String address;

    private BloodGroup bloodGroup;

    private String disease;
    private String allergies;

    private String emergencyContactName;
    private String emergencyContactPhone;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}