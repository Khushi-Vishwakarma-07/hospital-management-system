package com.hospital.management.hospitalmanagementsystem.appointment.entity;

import com.hospital.management.hospitalmanagementsystem.appointment.enums.AppointmentStatus;
import com.hospital.management.hospitalmanagementsystem.appointment.enums.AppointmentType;
import com.hospital.management.hospitalmanagementsystem.common.base.BaseEntity;
import com.hospital.management.hospitalmanagementsystem.doctor.entity.Doctor;
import com.hospital.management.hospitalmanagementsystem.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "appointments",
        indexes = {
                @Index(name = "idx_appointment_datetime", columnList = "appointmentDateTime"),
                @Index(name = "idx_doctor_datetime", columnList = "doctor_id, appointmentDateTime"),
                @Index(name = "idx_patient_datetime", columnList = "patient_id, appointmentDateTime")
        }
)
public class Appointment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentType type;

    @Column(nullable = false)
    private LocalDateTime appointmentDateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    @Column(length = 500)
    private String reasonForVisit;

    @Column(length = 1000)
    private String notes;

    @Column(nullable = false)
    private Integer durationMinutes;

    private String roomNumber;

    @Column(nullable = false)
    private boolean followUpRequired;
}