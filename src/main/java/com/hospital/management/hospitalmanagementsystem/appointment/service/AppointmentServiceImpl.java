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
import com.hospital.management.hospitalmanagementsystem.doctor.entity.DoctorAvailability;
import com.hospital.management.hospitalmanagementsystem.doctor.repository.DoctorAvailabilityRepository;
import com.hospital.management.hospitalmanagementsystem.doctor.repository.DoctorRepository;
import com.hospital.management.hospitalmanagementsystem.patient.entity.Patient;
import com.hospital.management.hospitalmanagementsystem.patient.repository.PatientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorAvailabilityRepository availabilityRepository;

    @Override
    @Transactional
    public AppointmentResponseDTO createAppointment(
            AppointmentRequestDTO dto) {

        Patient patient = getPatient(dto.getPatientId());
        Doctor doctor = getDoctor(dto.getDoctorId());

        validateAppointment(dto);
        checkDoctorAvailability(dto);
        checkOverlap(dto, null);

        Appointment appointment =
                AppointmentMapper.toEntity(dto, patient, doctor);

        appointment.setStatus(AppointmentStatus.SCHEDULED);

        return AppointmentMapper.toDTO(
                appointmentRepository.save(appointment));
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

    @Override
    @Transactional
    public AppointmentResponseDTO updateAppointment(
            Long id,
            AppointmentRequestDTO dto) {

        Appointment appointment =
                appointmentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Appointment not found with id: " + id));

        Patient patient = getPatient(dto.getPatientId());
        Doctor doctor = getDoctor(dto.getDoctorId());

        validateAppointment(dto);
        checkDoctorAvailability(dto);
        checkOverlap(dto, id);

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        AppointmentMapper.updateEntity(appointment, dto);

        return AppointmentMapper.toDTO(
                appointmentRepository.save(appointment));
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

        return appointmentRepository.findAllByPatient_Id(patientId)
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

        return appointmentRepository.findAllByDoctor_Id(doctorId)
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


    private void validateAppointment(AppointmentRequestDTO request) {

        if (request.getAppointmentDateTime() == null) {
            throw new BusinessException("Appointment time is required");
        }

        if (request.getDurationMinutes() == null ||
                request.getDurationMinutes() <= 0) {
            throw new BusinessException("Duration must be greater than 0");
        }

        if (request.getAppointmentDateTime().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Appointment cannot be scheduled in the past");
        }
    }

    private Patient getPatient(Long patientId) {

        return patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found with id: " + patientId));
    }

    private Doctor getDoctor(Long doctorId) {

        return doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with id: " + doctorId));
    }


    private void checkDoctorAvailability(AppointmentRequestDTO requestDTO) {

        LocalDateTime appointmentDateTime = requestDTO.getAppointmentDateTime();

        DayOfWeek appointmentDay = appointmentDateTime.getDayOfWeek();
        LocalTime appointmentStartTime = appointmentDateTime.toLocalTime();

        LocalTime appointmentEndTime =
                appointmentStartTime.plusMinutes(requestDTO.getDurationMinutes());

        List<DoctorAvailability> availabilitySlots =
                availabilityRepository
                        .findByDoctor_IdAndDayOfWeek(
                                requestDTO.getDoctorId(),
                                appointmentDay
                        );

        if (availabilitySlots.isEmpty()) {
            throw new BusinessException(
                    "Doctor is not available on " + appointmentDay
            );
        }

        boolean slotFound = availabilitySlots.stream()
                .anyMatch(slot ->
                        !appointmentStartTime.isBefore(slot.getStartTime())
                                && !appointmentEndTime.isAfter(slot.getEndTime())
                );

        if (!slotFound) {
            throw new BusinessException(
                    "Requested time is outside doctor's availability"
            );
        }
    }

    private void checkOverlap(
            AppointmentRequestDTO dto,
            Long excludeId) {

        LocalDateTime newStart = dto.getAppointmentDateTime();
        LocalDateTime newEnd =
                newStart.plusMinutes(dto.getDurationMinutes());

        boolean overlapExists =
                appointmentRepository.findAllByDoctor_Id(dto.getDoctorId())
                        .stream()
                        .filter(a -> a.getStatus() != AppointmentStatus.CANCELLED)
                        .filter(a -> !java.util.Objects.equals(a.getId(), excludeId))
                        .anyMatch(a -> {

                            LocalDateTime existingStart =
                                    a.getAppointmentDateTime();

                            LocalDateTime existingEnd =
                                    existingStart.plusMinutes(
                                            a.getDurationMinutes());

                            return newStart.isBefore(existingEnd)
                                    && newEnd.isAfter(existingStart);
                        });

        if (overlapExists) {
            throw new BusinessException(
                    "Doctor already has an appointment during this time");
        }
    }

}