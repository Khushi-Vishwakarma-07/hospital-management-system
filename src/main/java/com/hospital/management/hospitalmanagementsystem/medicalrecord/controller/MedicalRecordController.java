package com.hospital.management.hospitalmanagementsystem.medicalrecord.controller;

import com.hospital.management.hospitalmanagementsystem.medicalrecord.dto.MedicalRecordRequestDTO;
import com.hospital.management.hospitalmanagementsystem.medicalrecord.dto.MedicalRecordResponseDTO;
import com.hospital.management.hospitalmanagementsystem.medicalrecord.dto.MedicalRecordUpdateDTO;
import com.hospital.management.hospitalmanagementsystem.medicalrecord.service.MedicalRecordService;
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

@RestController
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
@Validated
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @PostMapping
    public ResponseEntity<MedicalRecordResponseDTO> create(
            @Valid @RequestBody MedicalRecordRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(medicalRecordService.createMedicalRecord(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordResponseDTO> getById(
            @PathVariable
            @Positive(message = "Medical record ID must be a positive number")
            Long id) {

        return ResponseEntity.ok(medicalRecordService.getMedicalRecordById(id));
    }

    @GetMapping
    public ResponseEntity<Page<MedicalRecordResponseDTO>> getAll(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {

        return ResponseEntity.ok(
                medicalRecordService.getAllMedicalRecords(pageable)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecordResponseDTO> update(
            @PathVariable
            @Positive(message = "Medical record ID must be a positive number")
            Long id,
            @Valid @RequestBody MedicalRecordUpdateDTO dto) {

        return ResponseEntity.ok(
                medicalRecordService.updateMedicalRecord(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable
            @Positive(message = "Medical record ID must be a positive number")
            Long id) {

        medicalRecordService.deleteMedicalRecord(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<Page<MedicalRecordResponseDTO>> getMedicalRecordsByPatient(
            @PathVariable
            @Positive(message = "Patient ID must be a positive number")
            Long patientId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {

        return ResponseEntity.ok(
                medicalRecordService.getMedicalRecordsByPatient(patientId, pageable)
        );
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<Page<MedicalRecordResponseDTO>> getMedicalRecordsByDoctor(
            @PathVariable
            @Positive(message = "Doctor ID must be a positive number")
            Long doctorId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {

        return ResponseEntity.ok(
                medicalRecordService.getMedicalRecordsByDoctor(doctorId, pageable)
        );
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<MedicalRecordResponseDTO> getMedicalRecordByAppointment(
            @PathVariable
            @Positive(message = "Appointment ID must be a positive number")
            Long appointmentId) {

        return ResponseEntity.ok(
                medicalRecordService.getMedicalRecordByAppointment(appointmentId)
        );
    }
}