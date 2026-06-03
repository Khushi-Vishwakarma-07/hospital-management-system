package com.hospital.management.hospitalmanagementsystem.appointment.dto;

import com.hospital.management.hospitalmanagementsystem.appointment.enums.AppointmentType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AppointmentRequestDTO {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotNull(message = "Appointment type is required")
    private AppointmentType type;

    @NotNull(message = "Appointment date and time is required")
    @Future(message = "Appointment must be scheduled in the future")
    private LocalDateTime appointmentDateTime;

    @Size(max = 500, message = "Reason for visit cannot exceed 500 characters")
    private String reasonForVisit;

    @Size(max = 1000, message = "Notes cannot exceed 1000 characters")
    private String notes;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer durationMinutes;

    @Size(max = 50, message = "Room number cannot exceed 50 characters")
    private String roomNumber;

    private boolean followUpRequired;
}