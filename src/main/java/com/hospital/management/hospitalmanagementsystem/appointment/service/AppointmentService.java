package com.hospital.management.hospitalmanagementsystem.appointment.service;

import com.hospital.management.hospitalmanagementsystem.appointment.dto.AppointmentRequestDTO;
import com.hospital.management.hospitalmanagementsystem.appointment.dto.AppointmentResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {

    AppointmentResponseDTO createAppointment(AppointmentRequestDTO requestDTO);

    AppointmentResponseDTO getAppointmentById(Long id);

    List<AppointmentResponseDTO> getAllAppointments();

    AppointmentResponseDTO updateAppointment(Long id, AppointmentRequestDTO requestDTO);

    void deleteAppointment(Long id);

    List<AppointmentResponseDTO> getAppointmentsByPatientId(Long patientId);

    List<AppointmentResponseDTO> getAppointmentsByDoctorId(Long doctorId);

    List<AppointmentResponseDTO> getAppointmentsByDateRange(
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}