package com.hospital.management.hospitalmanagementsystem.prescription.controller;

import com.hospital.management.hospitalmanagementsystem.prescription.dto.PrescriptionRequestDTO;
import com.hospital.management.hospitalmanagementsystem.prescription.dto.PrescriptionResponseDTO;
import com.hospital.management.hospitalmanagementsystem.prescription.dto.PrescriptionUpdateDTO;
import com.hospital.management.hospitalmanagementsystem.prescription.service.PrescriptionService;
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
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
@Validated
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @PostMapping
    public ResponseEntity<PrescriptionResponseDTO> create(@Valid @RequestBody PrescriptionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(prescriptionService.createPrescription(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionResponseDTO> getById(
            @PathVariable @Positive(message = "Prescription ID must be a positive number") Long id) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionById(id));
    }

    @GetMapping("/by-medical-record/{medicalRecordId}")
    public ResponseEntity<PrescriptionResponseDTO> getByMedicalRecordId(
            @PathVariable @Positive(message = "Medical record ID must be a positive number") Long medicalRecordId) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionByMedicalRecordId(medicalRecordId));
    }

    @GetMapping
    public ResponseEntity<Page<PrescriptionResponseDTO>> getAll(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(prescriptionService.getAllPrescriptions(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrescriptionResponseDTO> update(
            @PathVariable @Positive(message = "Prescription ID must be a positive number") Long id,
            @Valid @RequestBody PrescriptionUpdateDTO dto) {
        return ResponseEntity.ok(prescriptionService.updatePrescription(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable @Positive(message = "Prescription ID must be a positive number") Long id) {
        prescriptionService.deletePrescription(id);
        return ResponseEntity.noContent().build();
    }
}