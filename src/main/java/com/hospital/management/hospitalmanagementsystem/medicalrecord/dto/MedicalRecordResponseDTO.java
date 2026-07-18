package com.hospital.management.hospitalmanagementsystem.medicalrecord.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordResponseDTO {

    private Long id;

    private Long patientId;
    private String patientName;

    private Long doctorId;
    private String doctorName;

    private Long appointmentId;

    private String chiefComplaint;
    private String diagnosis;
    private String clinicalNotes;

    private Double height;
    private Double weight;

    private Integer systolicBloodPressure;
    private Integer diastolicBloodPressure;

    private Double temperature;
    private Integer heartRate;
    private Integer respiratoryRate;
    private Integer oxygenSaturation;

    private LocalDate followUpDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}