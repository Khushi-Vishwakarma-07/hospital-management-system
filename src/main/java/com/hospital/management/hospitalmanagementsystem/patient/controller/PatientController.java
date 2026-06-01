package com.hospital.management.hospitalmanagementsystem.patient.controller;

import com.hospital.management.hospitalmanagementsystem.patient.dto.PatientRequestDTO;
import com.hospital.management.hospitalmanagementsystem.patient.dto.PatientResponseDTO;
import com.hospital.management.hospitalmanagementsystem.patient.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<PatientResponseDTO> create(@Valid @RequestBody PatientRequestDTO dto) {

        PatientResponseDTO responseDTO = patientService.createPatient(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> getPatient(@PathVariable Long id) {

        return ResponseEntity.ok(
                patientService.getPatientById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> getAll() {

        return ResponseEntity.ok(
                patientService.getAllPatients()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> update(@PathVariable Long id, @Valid @RequestBody PatientRequestDTO dto) {

        return ResponseEntity.ok(
                patientService.updatePatient(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        patientService.deletePatient(id);

        return ResponseEntity.noContent().build();
    }
}