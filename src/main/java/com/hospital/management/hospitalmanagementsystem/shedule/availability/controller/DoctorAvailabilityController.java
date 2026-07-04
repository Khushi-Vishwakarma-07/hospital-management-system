package com.hospital.management.hospitalmanagementsystem.shedule.availability.controller;

import com.hospital.management.hospitalmanagementsystem.shedule.availability.dto.AvailabilityRequestDTO;
import com.hospital.management.hospitalmanagementsystem.shedule.availability.dto.AvailabilityResponseDTO;
import com.hospital.management.hospitalmanagementsystem.shedule.availability.service.DoctorAvailabilityService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;

@RestController
@RequestMapping("/api/doctor-availabilities")
@RequiredArgsConstructor
@Validated
public class DoctorAvailabilityController {

    private final DoctorAvailabilityService service;

    @PostMapping("/doctor/{doctorId}")
    public ResponseEntity<AvailabilityResponseDTO> create(
            @PathVariable @Positive Long doctorId,
            @Valid @RequestBody AvailabilityRequestDTO request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createAvailability(doctorId, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvailabilityResponseDTO> getById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(service.getAvailabilityById(id));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AvailabilityResponseDTO>> getByDoctor(
            @PathVariable @Positive Long doctorId,
            @RequestParam(required = false) DayOfWeek dayOfWeek) {

        return ResponseEntity.ok(service.getDoctorAvailabilities(doctorId, dayOfWeek));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AvailabilityResponseDTO> update(
            @PathVariable @Positive Long id,
            @Valid @RequestBody AvailabilityRequestDTO request) {

        return ResponseEntity.ok(service.updateAvailability(id, request));
    }

    @PutMapping("/doctor/{doctorId}/schedule")
    public ResponseEntity<List<AvailabilityResponseDTO>> replaceWeeklySchedule(
            @PathVariable @Positive Long doctorId,
            @Valid @RequestBody List<@Valid AvailabilityRequestDTO> requests) {

        return ResponseEntity.ok(service.replaceWeeklySchedule(doctorId, requests));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Positive Long id) {
        service.deleteAvailability(id);
        return ResponseEntity.noContent().build();
    }
}