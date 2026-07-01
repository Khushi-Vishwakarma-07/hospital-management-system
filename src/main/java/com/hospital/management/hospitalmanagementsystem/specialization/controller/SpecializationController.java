package com.hospital.management.hospitalmanagementsystem.specialization.controller;

import com.hospital.management.hospitalmanagementsystem.specialization.dto.SpecializationRequestDTO;
import com.hospital.management.hospitalmanagementsystem.specialization.dto.SpecializationResponseDTO;
import com.hospital.management.hospitalmanagementsystem.specialization.service.SpecializationService;
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
@RequestMapping("/api/specializations")
@RequiredArgsConstructor
@Validated
public class SpecializationController {

    private final SpecializationService specializationService;

    @PostMapping
    public ResponseEntity<SpecializationResponseDTO> create(
            @Valid @RequestBody SpecializationRequestDTO request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(specializationService.createSpecialization(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpecializationResponseDTO> getById(
            @PathVariable
            @Positive(message = "Specialization ID must be a positive number")
            Long id) {

        return ResponseEntity.ok(specializationService.getSpecializationById(id));
    }

    @GetMapping
    public ResponseEntity<Page<SpecializationResponseDTO>> getAll(
            @PageableDefault(size = 20, sort = "name")
            Pageable pageable) {

        return ResponseEntity.ok(specializationService.getAllSpecializations(pageable));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<Page<SpecializationResponseDTO>> getByDepartment(
            @PathVariable
            @Positive(message = "Department ID must be a positive number")
            Long departmentId,

            @PageableDefault(size = 20, sort = "name")
            Pageable pageable) {

        return ResponseEntity.ok(
                specializationService.getSpecializationsByDepartment(departmentId, pageable)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpecializationResponseDTO> update(
            @PathVariable
            @Positive(message = "Specialization ID must be a positive number")
            Long id,

            @Valid @RequestBody SpecializationRequestDTO request) {

        return ResponseEntity.ok(specializationService.updateSpecialization(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable
            @Positive(message = "Specialization ID must be a positive number")
            Long id) {

        specializationService.deleteSpecialization(id);
        return ResponseEntity.noContent().build();
    }
}