package com.hospital.management.hospitalmanagementsystem.medicalrecord.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordRequestDTO {

    @NotNull(message = "Patient ID is required")
    @Positive(message = "Patient ID must be positive")
    private Long patientId;

    @NotNull(message = "Doctor ID is required")
    @Positive(message = "Doctor ID must be positive")
    private Long doctorId;

    @NotNull(message = "Appointment ID is required")
    @Positive(message = "Appointment ID must be positive")
    private Long appointmentId;

    @NotBlank(message = "Chief complaint is required")
    @Size(max = 500, message = "Chief complaint must not exceed 500 characters")
    private String chiefComplaint;

    @NotBlank(message = "Diagnosis is required")
    @Size(max = 1000, message = "Diagnosis must not exceed 1000 characters")
    private String diagnosis;

    @Size(max = 10000, message = "Clinical notes must not exceed 10000 characters")
    private String clinicalNotes;

    @Positive(message = "Height must be positive")
    private Double height;

    @Positive(message = "Weight must be positive")
    private Double weight;

    @Positive(message = "Systolic blood pressure must be positive")
    private Integer systolicBloodPressure;

    @Positive(message = "Diastolic blood pressure must be positive")
    private Integer diastolicBloodPressure;

    @DecimalMin(value = "25.0", message = "Temperature is too low")
    @DecimalMax(value = "45.0", message = "Temperature is too high")
    private Double temperature;

    @Positive(message = "Heart rate must be positive")
    private Integer heartRate;

    @Positive(message = "Respiratory rate must be positive")
    private Integer respiratoryRate;

    @Min(value = 0, message = "Oxygen saturation cannot be negative")
    @Max(value = 100, message = "Oxygen saturation cannot exceed 100")
    private Integer oxygenSaturation;

    @FutureOrPresent(message = "Follow-up date cannot be in the past")
    private LocalDate followUpDate;
}