package com.hospital.management.hospitalmanagementsystem.prescription.service;

import com.hospital.management.hospitalmanagementsystem.prescription.dto.PrescriptionRequestDTO;
import com.hospital.management.hospitalmanagementsystem.prescription.dto.PrescriptionResponseDTO;
import com.hospital.management.hospitalmanagementsystem.prescription.dto.PrescriptionUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PrescriptionService {

    PrescriptionResponseDTO createPrescription(PrescriptionRequestDTO dto);

    PrescriptionResponseDTO getPrescriptionById(Long id);

    PrescriptionResponseDTO getPrescriptionByMedicalRecordId(Long medicalRecordId);

    Page<PrescriptionResponseDTO> getAllPrescriptions(Pageable pageable);

    PrescriptionResponseDTO updatePrescription(Long id, PrescriptionUpdateDTO dto);

    void deletePrescription(Long id);
}