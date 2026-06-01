package com.hospital.management.hospitalmanagementsystem.patient.dto;

import com.hospital.management.hospitalmanagementsystem.patient.enums.BloodGroup;
import com.hospital.management.hospitalmanagementsystem.patient.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientRequestDTO {

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

    @NotNull(message = "Age is required")
    @Min(value = 0, message = "Age cannot be negative")
    @Max(value = 130, message = "Invalid age")
    private Integer age;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Invalid phone number")
    private String phone;

    @Email(message = "Invalid email format")
    @Size(max = 150)
    private String email;

    @Size(max = 300)
    private String address;

    @NotNull(message = "Blood group is required")
    private BloodGroup bloodGroup;

    @Size(max = 500)
    private String disease;

    @Size(max = 500)
    private String allergies;

    @NotBlank(message = "Emergency contact name is required")
    @Size(max = 100, message = "Emergency contact name cannot exceed 100 characters")
    @Pattern(
            regexp = "^[A-Za-z]+(?:[ '-][A-Za-z]+)*$",
            message = "Emergency contact name must contain only letters"
    )
    private String emergencyContactName;

    @NotBlank(message = "Emergency contact phone is required")
    @Pattern(regexp = "^(\\+91)?[6-9][0-9]{9}$", message = "Invalid Indian mobile number")
    private String emergencyContactPhone;
}