package com.hospital.management.hospitalmanagementsystem.appointment.service;

import com.hospital.management.hospitalmanagementsystem.appointment.dto.AppointmentRequestDTO;
import com.hospital.management.hospitalmanagementsystem.appointment.dto.AppointmentResponseDTO;
import com.hospital.management.hospitalmanagementsystem.appointment.entity.Appointment;
import com.hospital.management.hospitalmanagementsystem.appointment.enums.AppointmentStatus;
import com.hospital.management.hospitalmanagementsystem.appointment.mapper.AppointmentMapper;
import com.hospital.management.hospitalmanagementsystem.appointment.repository.AppointmentRepository;
import com.hospital.management.hospitalmanagementsystem.common.exception.ResourceNotFoundException;
import com.hospital.management.hospitalmanagementsystem.doctor.entity.Doctor;
import com.hospital.management.hospitalmanagementsystem.doctor.repository.DoctorRepository;
import com.hospital.management.hospitalmanagementsystem.patient.entity.Patient;
import com.hospital.management.hospitalmanagementsystem.patient.repository.PatientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @Transactional
    @Override
    public AppointmentResponseDTO createAppointment(AppointmentRequestDTO requestDTO) {

        Patient patient = patientRepository.findById(requestDTO.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient not found with id: " + requestDTO.getPatientId()));

        Doctor doctor = doctorRepository.findById(requestDTO.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doctor not found with id: " + requestDTO.getDoctorId()));

        Appointment appointment = AppointmentMapper.toEntity(requestDTO, patient, doctor);

        appointment.setStatus(AppointmentStatus.SCHEDULED);

        Appointment saved = appointmentRepository.save(appointment);

        return AppointmentMapper.toDTO(saved);
    }

    @Override
    public AppointmentResponseDTO getAppointmentById(Long id) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appointment not found with id: " + id));

        return AppointmentMapper.toDTO(appointment);
    }

    @Override
    public List<AppointmentResponseDTO> getAllAppointments() {
        return appointmentRepository.findAll()
                .stream()
                .map(AppointmentMapper::toDTO)
                .toList();
    }

    @Transactional
    @Override
    public AppointmentResponseDTO updateAppointment(Long id, AppointmentRequestDTO requestDTO) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appointment not found with id: " + id));

        Patient patient = patientRepository.findById(requestDTO.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient not found with id: " + requestDTO.getPatientId()));

        Doctor doctor = doctorRepository.findById(requestDTO.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doctor not found with id: " + requestDTO.getDoctorId()));

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        AppointmentMapper.updateEntity(appointment, requestDTO);

        Appointment updated = appointmentRepository.save(appointment);

        return AppointmentMapper.toDTO(updated);
    }

    @Transactional
    @Override
    public void deleteAppointment(Long id) {

        if (!appointmentRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Appointment not found with id: " + id);
        }

        appointmentRepository.deleteById(id);
    }

    @Override
    public List<AppointmentResponseDTO> getAppointmentsByPatientId(Long patientId) {

        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException(
                    "Patient not found with id: " + patientId);
        }

        return appointmentRepository.findAllByPatientId(patientId)
                .stream()
                .map(AppointmentMapper::toDTO)
                .toList();
    }

    @Override
    public List<AppointmentResponseDTO> getAppointmentsByDoctorId(Long doctorId) {

        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException(
                    "Doctor not found with id: " + doctorId);
        }

        return appointmentRepository.findAllByDoctorId(doctorId)
                .stream()
                .map(AppointmentMapper::toDTO)
                .toList();
    }

    @Override
    public List<AppointmentResponseDTO> getAppointmentsByDateRange(
            LocalDateTime startDate,
            LocalDateTime endDate) {

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException(
                    "Start date and end date must not be null");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(
                    "Start date must be before end date");
        }

        return appointmentRepository
                .findAllByAppointmentDateTimeBetween(startDate, endDate)
                .stream()
                .map(AppointmentMapper::toDTO)
                .toList();
    }
}