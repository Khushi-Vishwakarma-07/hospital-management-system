package com.hospital.management.hospitalmanagementsystem.patient.service;

import com.hospital.management.hospitalmanagementsystem.patient.dto.PatientRequestDTO;
import com.hospital.management.hospitalmanagementsystem.patient.dto.PatientResponseDTO;

import java.util.List;

public interface PatientService {

    PatientResponseDTO createPatient(PatientRequestDTO dto);

    PatientResponseDTO getPatientById(Long id);

    List<PatientResponseDTO> getAllPatients();

    PatientResponseDTO updatePatient(Long id, PatientRequestDTO dto);

    void deletePatient(Long id);
}