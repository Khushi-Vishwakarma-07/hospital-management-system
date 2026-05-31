package com.hospital.management.hospitalmanagementsystem.patient.controller;

import com.hospital.management.hospitalmanagementsystem.patient.dto.PatientRequestDTO;
import com.hospital.management.hospitalmanagementsystem.patient.dto.PatientResponseDTO;
import com.hospital.management.hospitalmanagementsystem.patient.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public PatientResponseDTO create(@RequestBody PatientRequestDTO patientRequestDTO) {
        return patientService.createPatient(patientRequestDTO);
    }

    @GetMapping("/{id}")
    public PatientResponseDTO getPatient(@PathVariable Long id) {
        return patientService.getPatientById(id);
    }

    @GetMapping
    public List<PatientResponseDTO> getAll() {
        return patientService.getAllPatients();
    }

    @PutMapping("/{id}")
    public PatientResponseDTO update(@PathVariable Long id, @RequestBody PatientRequestDTO patientRequestDTO) {
        return patientService.updatePatient(id, patientRequestDTO);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        patientService.deletePatient(id);
        return "Patient deleted successfully";
    }
}


