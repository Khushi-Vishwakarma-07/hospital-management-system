package com.hospital.management.hospitalmanagementsystem.appointment.mapper;

import com.hospital.management.hospitalmanagementsystem.appointment.dto.AppointmentRequestDTO;
import com.hospital.management.hospitalmanagementsystem.appointment.dto.AppointmentResponseDTO;
import com.hospital.management.hospitalmanagementsystem.appointment.entity.Appointment;
import com.hospital.management.hospitalmanagementsystem.appointment.enums.AppointmentStatus;
import com.hospital.management.hospitalmanagementsystem.doctor.entity.Doctor;
import com.hospital.management.hospitalmanagementsystem.patient.entity.Patient;

public class AppointmentMapper {

    private AppointmentMapper() {
    }

    public static Appointment toEntity(
            AppointmentRequestDTO dto,
            Patient patient,
            Doctor doctor
    ) {

        return Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .type(dto.getType())
                .appointmentDateTime(dto.getAppointmentDateTime())
                .reasonForVisit(dto.getReasonForVisit())
                .notes(dto.getNotes())
                .durationMinutes(dto.getDurationMinutes())
                .roomNumber(dto.getRoomNumber())
                .followUpRequired(dto.isFollowUpRequired())
                .build();
    }

    public static AppointmentResponseDTO toDTO(Appointment appointment) {
        return AppointmentResponseDTO.builder()
                .id(appointment.getId())
                .patientId(appointment.getPatient().getId())
                .patientName(appointment.getPatient().getFullName())
                .doctorId(appointment.getDoctor().getId())
                .doctorName(appointment.getDoctor().getFullName())
                .type(appointment.getType())
                .appointmentDateTime(appointment.getAppointmentDateTime())
                .status(appointment.getStatus())
                .reasonForVisit(appointment.getReasonForVisit())
                .notes(appointment.getNotes())
                .durationMinutes(appointment.getDurationMinutes())
                .roomNumber(appointment.getRoomNumber())
                .followUpRequired(appointment.isFollowUpRequired())
                .createdAt(appointment.getCreatedAt())
                .updatedAt(appointment.getUpdatedAt())
                .build();
    }


    public static void updateEntity(Appointment appointment, AppointmentRequestDTO requestDTO) {
        appointment.setType(requestDTO.getType());
        appointment.setAppointmentDateTime(requestDTO.getAppointmentDateTime());
        appointment.setReasonForVisit(requestDTO.getReasonForVisit());
        appointment.setNotes(requestDTO.getNotes());
        appointment.setDurationMinutes(requestDTO.getDurationMinutes());
        appointment.setRoomNumber(requestDTO.getRoomNumber());
        appointment.setFollowUpRequired(requestDTO.isFollowUpRequired());
    }
}