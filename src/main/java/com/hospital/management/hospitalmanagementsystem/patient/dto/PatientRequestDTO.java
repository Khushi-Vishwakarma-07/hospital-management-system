package com.hospital.management.hospitalmanagementsystem.patient.dto;

import com.hospital.management.hospitalmanagementsystem.patient.enums.BloodGroup;
import com.hospital.management.hospitalmanagementsystem.patient.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientRequestDTO {

    // e.g. "O'Brien", "Mary-Jane"
    private static final String NAME_REGEX    = "^[A-Za-z]+(?:[ '\\-][A-Za-z]+)*$";

    // e.g. "9876543210", "+919876543210"
    private static final String PHONE_REGEX   = "^(?:\\+91|0)?[6-9][0-9]{9}$";
    private static final String PHONE_MESSAGE = "Must be a valid Indian mobile number (e.g. 9876543210 or +919876543210)";

    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 100, message = "First name must be between 1 and 100 characters")
    @Pattern(regexp = NAME_REGEX, message = "First name must contain only letters, spaces, hyphens, or apostrophes")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 100, message = "Last name must be between 1 and 100 characters")
    @Pattern(regexp = NAME_REGEX, message = "Last name must contain only letters, spaces, hyphens, or apostrophes")
    private String lastName;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = PHONE_REGEX, message = PHONE_MESSAGE)
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email address")
    @Size(max = 150, message = "Email must not exceed 150 characters")
    private String email;

    @Size(max = 300, message = "Address must not exceed 300 characters")
    private String address;

    @NotNull(message = "Blood group is required")
    private BloodGroup bloodGroup;

    @Size(max = 500, message = "Disease description must not exceed 500 characters")
    private String disease;

    @Size(max = 500, message = "Allergies description must not exceed 500 characters")
    private String allergies;

    @NotBlank(message = "Emergency contact name is required")
    @Size(min = 1, max = 100, message = "Emergency contact name must be between 1 and 100 characters")
    @Pattern(regexp = NAME_REGEX, message = "Emergency contact name must contain only letters, spaces, hyphens, or apostrophes")
    private String emergencyContactName;

    @NotBlank(message = "Emergency contact phone is required")
    @Pattern(regexp = PHONE_REGEX, message = PHONE_MESSAGE)
    private String emergencyContactPhone;
}