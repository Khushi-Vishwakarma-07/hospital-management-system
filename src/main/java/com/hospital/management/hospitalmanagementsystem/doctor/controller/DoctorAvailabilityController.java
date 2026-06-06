package com.hospital.management.hospitalmanagementsystem.doctor.controller;

import com.hospital.management.hospitalmanagementsystem.doctor.dto.AvailabilityRequestDTO;
import com.hospital.management.hospitalmanagementsystem.doctor.dto.AvailabilityResponseDTO;
import com.hospital.management.hospitalmanagementsystem.doctor.service.DoctorAvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor-availabilities")
@RequiredArgsConstructor
public class DoctorAvailabilityController {

    private final DoctorAvailabilityService service;

    @PostMapping("/doctor/{doctorId}")
    public ResponseEntity<AvailabilityResponseDTO> create(
            @PathVariable Long doctorId,
            @Valid @RequestBody AvailabilityRequestDTO request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createAvailability(doctorId, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvailabilityResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getAvailabilityById(id));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AvailabilityResponseDTO>> getByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(service.getDoctorAvailability(doctorId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AvailabilityResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody AvailabilityRequestDTO request) {

        return ResponseEntity.ok(service.updateAvailability(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteAvailability(id);
        return ResponseEntity.noContent().build();
    }
}