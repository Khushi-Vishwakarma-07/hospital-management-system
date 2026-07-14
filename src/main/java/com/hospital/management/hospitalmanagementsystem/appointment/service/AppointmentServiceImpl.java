package com.hospital.management.hospitalmanagementsystem.appointment.service;

import com.hospital.management.hospitalmanagementsystem.appointment.dto.AppointmentRequestDTO;
import com.hospital.management.hospitalmanagementsystem.appointment.dto.AppointmentResponseDTO;
import com.hospital.management.hospitalmanagementsystem.appointment.entity.Appointment;
import com.hospital.management.hospitalmanagementsystem.appointment.enums.AppointmentStatus;
import com.hospital.management.hospitalmanagementsystem.appointment.mapper.AppointmentMapper;
import com.hospital.management.hospitalmanagementsystem.appointment.repository.AppointmentRepository;
import com.hospital.management.hospitalmanagementsystem.common.exception.BusinessException;
import com.hospital.management.hospitalmanagementsystem.common.exception.ResourceNotFoundException;
import com.hospital.management.hospitalmanagementsystem.doctor.entity.Doctor;
import com.hospital.management.hospitalmanagementsystem.schedule.availability.repository.DoctorAvailabilityRepository;
import com.hospital.management.hospitalmanagementsystem.schedule.leave.enums.LeaveStatus;
import com.hospital.management.hospitalmanagementsystem.schedule.leave.repository.DoctorLeaveRepository;
import com.hospital.management.hospitalmanagementsystem.doctor.repository.DoctorRepository;
import com.hospital.management.hospitalmanagementsystem.patient.entity.Patient;
import com.hospital.management.hospitalmanagementsystem.patient.repository.PatientRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private static final long MAX_APPOINTMENT_DURATION_MINUTES = 120;

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorAvailabilityRepository availabilityRepository;
    private final DoctorLeaveRepository doctorLeaveRepository;

    @Override
    @Transactional
    public AppointmentResponseDTO createAppointment(
            AppointmentRequestDTO dto) {

        Patient patient = getPatient(dto.getPatientId());

        Doctor doctor = doctorRepository.findByIdForUpdate(dto.getDoctorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with id: " + dto.getDoctorId()));

        validateScheduling(dto, null);

        Appointment appointment =
                AppointmentMapper.toEntity(dto, patient, doctor);

        appointment.setStatus(AppointmentStatus.SCHEDULED);

        return AppointmentMapper.toDTO(
                appointmentRepository.save(appointment));
    }

    @Transactional(readOnly = true)
    @Override
    public AppointmentResponseDTO getAppointmentById(Long id) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appointment not found with id: " + id));

        return AppointmentMapper.toDTO(appointment);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<AppointmentResponseDTO> getAllAppointments(
            Pageable pageable) {

        return appointmentRepository.findAll(pageable)
                .map(AppointmentMapper::toDTO);
    }

    @Override
    @Transactional
    public AppointmentResponseDTO updateAppointment(
            Long id,
            AppointmentRequestDTO dto) {

        Appointment appointment = getAppointmentForUpdate(id);

        Patient patient = getPatient(dto.getPatientId());

        Doctor doctor = doctorRepository.findByIdForUpdate(dto.getDoctorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with id: "
                                        + dto.getDoctorId()));

        validateScheduling(dto, id);

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        AppointmentMapper.updateEntity(appointment, dto);

        return AppointmentMapper.toDTO(
                appointmentRepository.save(appointment));
    }

    @Override
    @Transactional
    public void deleteAppointment(Long id) {

        Appointment appointment = getAppointmentForUpdate(id);

        appointmentRepository.delete(appointment);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<AppointmentResponseDTO> getAppointmentsByPatientId(
            Long patientId,
            Pageable pageable) {

        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException(
                    "Patient not found with id: " + patientId);
        }

        return appointmentRepository
                .findAllByPatient_Id(patientId, pageable)
                .map(AppointmentMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<AppointmentResponseDTO> getAppointmentsByDoctorId(
            Long doctorId,
            Pageable pageable) {

        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException(
                    "Doctor not found with id: " + doctorId);
        }

        return appointmentRepository
                .findAllByDoctor_Id(doctorId, pageable)
                .map(AppointmentMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<AppointmentResponseDTO> getAppointmentsByDateRange(
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable) {

        if (startDate == null || endDate == null) {
            throw new BusinessException(
                    "Start date and end date must not be null");
        }

        if (startDate.isAfter(endDate)) {
            throw new BusinessException(
                    "Start date must be before end date");
        }

        return appointmentRepository
                .findAllByAppointmentDateTimeBetween(
                        startDate,
                        endDate,
                        pageable)
                .map(AppointmentMapper::toDTO);
    }

    @Override
    @Transactional
    public AppointmentResponseDTO updateStatus(
            Long appointmentId,
            AppointmentStatus newStatus) {

        Appointment appointment = getAppointmentForUpdate(appointmentId);

        if (!appointment.getStatus().canTransitionTo(newStatus)) {
            throw new BusinessException(
                    "Cannot transition appointment from "
                            + appointment.getStatus()
                            + " to "
                            + newStatus);
        }

        appointment.setStatus(newStatus);

        return AppointmentMapper.toDTO(
                appointmentRepository.save(appointment));
    }

    // ================= PRIVATE METHODS =================

    private void validateScheduling(
            AppointmentRequestDTO dto,
            Long excludeAppointmentId) {

        validateAppointment(dto);
        checkDoctorNotOnLeave(dto);
        checkDoctorAvailability(dto);
        checkOverlap(dto, excludeAppointmentId);
    }

    private void validateAppointment(AppointmentRequestDTO request) {

        if (request.getAppointmentDateTime() == null) {
            throw new BusinessException("Appointment time is required");
        }

        if (request.getDurationMinutes() == null
                || request.getDurationMinutes() <= 0) {
            throw new BusinessException("Duration must be greater than 0");
        }

        if (request.getDurationMinutes() > MAX_APPOINTMENT_DURATION_MINUTES) {
            throw new BusinessException(
                    "Appointment duration exceeds maximum allowed");
        }

        if (request.getAppointmentDateTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException(
                    "Appointment cannot be scheduled in the past");
        }
    }

    private Patient getPatient(Long patientId) {

        return patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found with id: " + patientId));
    }

    private void checkDoctorNotOnLeave(AppointmentRequestDTO requestDTO) {

        LocalDate appointmentDate =
                requestDTO.getAppointmentDateTime().toLocalDate();

        boolean onLeave = doctorLeaveRepository.existsOverlappingLeave(
                requestDTO.getDoctorId(),
                List.of(LeaveStatus.APPROVED),
                appointmentDate,
                appointmentDate,
                null);

        if (onLeave) {
            throw new BusinessException(
                    "Doctor is on approved leave on the requested date");
        }
    }

    private void checkDoctorAvailability(
            AppointmentRequestDTO requestDTO) {

        LocalDateTime appointmentDateTime =
                requestDTO.getAppointmentDateTime();

        DayOfWeek appointmentDay =
                appointmentDateTime.getDayOfWeek();

        LocalTime appointmentStartTime =
                appointmentDateTime.toLocalTime();

        LocalTime appointmentEndTime =
                appointmentStartTime.plusMinutes(
                        requestDTO.getDurationMinutes());

        if (!appointmentEndTime.isAfter(appointmentStartTime)) {
            throw new BusinessException(
                    "Appointment cannot span past midnight; "
                            + "please split it into separate appointments");
        }

        boolean available =
                availabilityRepository.existsAvailableSlot(
                        requestDTO.getDoctorId(),
                        appointmentDay,
                        appointmentStartTime,
                        appointmentEndTime);

        if (!available) {
            throw new BusinessException(
                    "Doctor is not available during the requested time");
        }
    }

    private Appointment getAppointmentForUpdate(Long id) {

        return appointmentRepository.findByIdForUpdate(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found with id: " + id));
    }

    private void checkOverlap(
            AppointmentRequestDTO dto,
            Long excludeId) {

        LocalDateTime newStart = dto.getAppointmentDateTime();
        LocalDateTime newEnd = newStart.plusMinutes(dto.getDurationMinutes());

        LocalDateTime startWindow =
                newStart.minusMinutes(MAX_APPOINTMENT_DURATION_MINUTES);

        boolean overlaps = appointmentRepository
                .findPotentialOverlappingAppointments(
                        dto.getDoctorId(),
                        startWindow,
                        newEnd,
                        excludeId)
                .stream()
                .anyMatch(existing -> {
                    LocalDateTime existingEnd =
                            existing.getAppointmentDateTime()
                                    .plusMinutes(existing.getDurationMinutes());

                    return existingEnd.isAfter(newStart);
                });

        if (overlaps) {
            throw new BusinessException(
                    "Doctor already has an appointment during this time");
        }
    }
}