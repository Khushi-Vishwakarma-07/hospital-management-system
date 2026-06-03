package com.hospital.management.hospitalmanagementsystem.appointment.repository;

import com.hospital.management.hospitalmanagementsystem.appointment.dto.AppointmentResponseDTO;
import com.hospital.management.hospitalmanagementsystem.appointment.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findAllByPatientId(Long patientId);

    List<Appointment> findAllByDoctorId(Long doctorId);

    List<Appointment> findAllByAppointmentDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
}
