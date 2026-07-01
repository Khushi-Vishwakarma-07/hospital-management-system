package com.hospital.management.hospitalmanagementsystem.doctor.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorRequestDTO {

    private static final String NAME_REGEX =
            "^[A-Za-z]+(?:[ '\\-][A-Za-z]+)*$";

    private static final String PHONE_REGEX =
            "^(?:\\+91|0)?[6-9][0-9]{9}$";

    private static final String PHONE_MESSAGE =
            "Must be a valid Indian mobile number (e.g. 9876543210 or +919876543210)";

    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 100, message = "First name must be between 1 and 100 characters")
    @Pattern(
            regexp = NAME_REGEX,
            message = "First name must contain only letters, spaces, hyphens, or apostrophes"
    )
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 100, message = "Last name must be between 1 and 100 characters")
    @Pattern(
            regexp = NAME_REGEX,
            message = "Last name must contain only letters, spaces, hyphens, or apostrophes"
    )
    private String lastName;

    @NotNull(message = "Specialization is required")
    @Positive(message = "Specialization id must be positive")
    private Long specializationId;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = PHONE_REGEX,
            message = PHONE_MESSAGE
    )
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email address")
    @Size(max = 150, message = "Email must not exceed 150 characters")
    private String email;

    @NotNull(message = "Experience is required")
    @Min(value = 0, message = "Experience cannot be negative")
    @Max(value = 70, message = "Experience cannot exceed 70 years")
    private Integer experienceYears;
}