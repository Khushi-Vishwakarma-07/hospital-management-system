package com.hospital.management.hospitalmanagementsystem.appointment.dto;

import com.hospital.management.hospitalmanagementsystem.appointment.enums.AppointmentStatus;
import com.hospital.management.hospitalmanagementsystem.appointment.enums.AppointmentType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AppointmentResponseDTO {

    private Long id;

    private Long patientId;
    private String patientName;

    private Long doctorId;
    private String doctorName;

    private AppointmentType type;

    private LocalDateTime appointmentDateTime;

    private AppointmentStatus status;

    private String reasonForVisit;

    private String notes;

    private Integer durationMinutes;

    private String roomNumber;

    private boolean followUpRequired;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}