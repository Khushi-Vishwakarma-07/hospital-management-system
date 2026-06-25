package com.hospital.management.hospitalmanagementsystem.patient.service;

import com.hospital.management.hospitalmanagementsystem.patient.dto.PatientRequestDTO;
import com.hospital.management.hospitalmanagementsystem.patient.dto.PatientResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatientService {

    PatientResponseDTO createPatient(PatientRequestDTO dto);

    PatientResponseDTO getPatientById(Long id);

    Page<PatientResponseDTO> getAllPatients(Pageable pageable);

    PatientResponseDTO updatePatient(Long id, PatientRequestDTO dto);

    void deletePatient(Long id);
}