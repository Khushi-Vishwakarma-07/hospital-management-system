package com.hospital.management.hospitalmanagementsystem.doctor.controller;

import com.hospital.management.hospitalmanagementsystem.doctor.dto.DoctorRequestDTO;
import com.hospital.management.hospitalmanagementsystem.doctor.dto.DoctorResponseDTO;
import com.hospital.management.hospitalmanagementsystem.doctor.service.DoctorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@Validated
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    public ResponseEntity<DoctorResponseDTO> create(
            @Valid @RequestBody DoctorRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(doctorService.createDoctor(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> getById(
            @PathVariable @Positive(message = "Doctor ID must be a positive number") Long id) {

        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @GetMapping
    public ResponseEntity<Page<DoctorResponseDTO>> getAll(
            @PageableDefault(size = 20, sort = "lastName") Pageable pageable) {

        return ResponseEntity.ok(doctorService.getAllDoctors(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> update(
            @PathVariable @Positive(message = "Doctor ID must be a positive number") Long id,
            @Valid @RequestBody DoctorRequestDTO dto) {

        return ResponseEntity.ok(doctorService.updateDoctor(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable @Positive(message = "Doctor ID must be a positive number") Long id) {

        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }
}