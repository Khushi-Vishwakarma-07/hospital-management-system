package com.hospital.management.hospitalmanagementsystem.doctor.dto;

import com.hospital.management.hospitalmanagementsystem.doctor.enums.DoctorSpecialization;
import jakarta.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorRequestDTO {

    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name cannot exceed 100 characters")
    @Pattern(
            regexp = "^[A-Za-z]+(?:[ '-][A-Za-z]+)*$",
            message = "First name must contain only letters"
    )
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name cannot exceed 100 characters")
    @Pattern(
            regexp = "^[A-Za-z]+(?:[ '-][A-Za-z]+)*$",
            message = "Last name must contain only letters"
    )
    private String lastName;

    @NotNull(message = "Specialization is required")
    private DoctorSpecialization specialization;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^(\\+91)?[6-9][0-9]{9}$",
            message = "Invalid Indian phone number"
    )
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 150, message = "Email cannot exceed 150 characters")
    private String email;

    @NotNull(message = "Experience is required")
    @Min(value = 0, message = "Experience cannot be negative")
    @Max(value = 70, message = "Experience cannot exceed 70 years")
    private Integer experienceYears;
}