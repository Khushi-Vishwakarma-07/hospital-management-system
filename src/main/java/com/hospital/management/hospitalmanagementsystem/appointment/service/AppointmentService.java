package com.hospital.management.hospitalmanagementsystem.appointment.service;

import com.hospital.management.hospitalmanagementsystem.appointment.dto.AppointmentRequestDTO;
import com.hospital.management.hospitalmanagementsystem.appointment.dto.AppointmentResponseDTO;
import com.hospital.management.hospitalmanagementsystem.appointment.enums.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface AppointmentService {

    AppointmentResponseDTO createAppointment(AppointmentRequestDTO requestDTO);

    AppointmentResponseDTO getAppointmentById(Long id);

    Page<AppointmentResponseDTO> getAllAppointments(Pageable pageable);

    AppointmentResponseDTO updateAppointment(Long id, AppointmentRequestDTO requestDTO);

    void deleteAppointment(Long id);

    Page<AppointmentResponseDTO> getAppointmentsByPatientId(
            Long patientId,
            Pageable pageable
    );

    Page<AppointmentResponseDTO> getAppointmentsByDoctorId(
            Long doctorId,
            Pageable pageable
    );

    Page<AppointmentResponseDTO> getAppointmentsByDateRange(
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    );

    AppointmentResponseDTO updateStatus(
            Long appointmentId,
            AppointmentStatus status
    );
}