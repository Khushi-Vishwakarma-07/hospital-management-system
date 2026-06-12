package com.hospital.management.hospitalmanagementsystem.appointment.controller;

import com.hospital.management.hospitalmanagementsystem.appointment.dto.AppointmentRequestDTO;
import com.hospital.management.hospitalmanagementsystem.appointment.dto.AppointmentResponseDTO;
import com.hospital.management.hospitalmanagementsystem.appointment.enums.AppointmentStatus;
import com.hospital.management.hospitalmanagementsystem.appointment.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> createAppointment(
            @Valid @RequestBody AppointmentRequestDTO requestDTO) {

        AppointmentResponseDTO responseDTO =
                appointmentService.createAppointment(requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> getAppointment(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                appointmentService.getAppointmentById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<AppointmentResponseDTO>> getAllAppointments() {

        return ResponseEntity.ok(
                appointmentService.getAllAppointments()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> updateAppointment(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentRequestDTO requestDTO) {

        return ResponseEntity.ok(
                appointmentService.updateAppointment(id, requestDTO)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(
            @PathVariable Long id) {

        appointmentService.deleteAppointment(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByPatientId(
            @PathVariable Long patientId) {

        return ResponseEntity.ok(
                appointmentService.getAppointmentsByPatientId(patientId)
        );
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByDoctorId(
            @PathVariable Long doctorId) {

        return ResponseEntity.ok(
                appointmentService.getAppointmentsByDoctorId(doctorId)
        );
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByDateRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {

        return ResponseEntity.ok(
                appointmentService.getAppointmentsByDateRange(
                        startDate,
                        endDate
                )
        );
    }

    @PatchMapping("/{appointmentId}/status")
    public ResponseEntity<AppointmentResponseDTO> updateStatus(
            @PathVariable Long appointmentId,
            @RequestParam AppointmentStatus status) {

        return ResponseEntity.ok(
                appointmentService.updateStatus(appointmentId, status)
        );
    }
}