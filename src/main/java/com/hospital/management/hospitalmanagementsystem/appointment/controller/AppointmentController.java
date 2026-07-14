package com.hospital.management.hospitalmanagementsystem.appointment.controller;

import com.hospital.management.hospitalmanagementsystem.appointment.dto.AppointmentRequestDTO;
import com.hospital.management.hospitalmanagementsystem.appointment.dto.AppointmentResponseDTO;
import com.hospital.management.hospitalmanagementsystem.appointment.enums.AppointmentStatus;
import com.hospital.management.hospitalmanagementsystem.appointment.service.AppointmentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/appointments")
@Validated
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
            @PathVariable @Positive Long id) {

        return ResponseEntity.ok(
                appointmentService.getAppointmentById(id)
        );
    }

    @GetMapping
    public ResponseEntity<Page<AppointmentResponseDTO>> getAllAppointments(
            @PageableDefault(size = 20, sort = "appointmentDateTime", direction = Sort.Direction.DESC)
            Pageable pageable) {

        return ResponseEntity.ok(
                appointmentService.getAllAppointments(pageable)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> updateAppointment(
            @PathVariable @Positive Long id,
            @Valid @RequestBody AppointmentRequestDTO requestDTO) {

        return ResponseEntity.ok(
                appointmentService.updateAppointment(id, requestDTO)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(
            @PathVariable @Positive Long id) {

        appointmentService.deleteAppointment(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<Page<AppointmentResponseDTO>> getAppointmentsByPatientId(
            @PathVariable @Positive Long patientId,
            @PageableDefault(size = 20, sort = "appointmentDateTime", direction = Sort.Direction.DESC)
            Pageable pageable) {

        return ResponseEntity.ok(
                appointmentService.getAppointmentsByPatientId(
                        patientId,
                        pageable
                )
        );
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<Page<AppointmentResponseDTO>> getAppointmentsByDoctorId(
            @PathVariable @Positive Long doctorId,
            @PageableDefault(size = 20, sort = "appointmentDateTime", direction = Sort.Direction.DESC)
            Pageable pageable) {

        return ResponseEntity.ok(
                appointmentService.getAppointmentsByDoctorId(
                        doctorId,
                        pageable
                )
        );
    }

    @GetMapping("/date-range")
    public ResponseEntity<Page<AppointmentResponseDTO>> getAppointmentsByDateRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            @PageableDefault(size = 20, sort = "appointmentDateTime", direction = Sort.Direction.ASC)
            Pageable pageable) {

        return ResponseEntity.ok(
                appointmentService.getAppointmentsByDateRange(
                        startDate,
                        endDate,
                        pageable
                )
        );
    }

    @PatchMapping("/{appointmentId}/status")
    public ResponseEntity<AppointmentResponseDTO> updateStatus(
            @PathVariable @Positive Long appointmentId,
            @RequestParam AppointmentStatus status) {

        return ResponseEntity.ok(
                appointmentService.updateStatus(
                        appointmentId,
                        status
                )
        );
    }
}