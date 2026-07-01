package com.hospital.management.hospitalmanagementsystem.specialization.service;

import com.hospital.management.hospitalmanagementsystem.specialization.dto.SpecializationRequestDTO;
import com.hospital.management.hospitalmanagementsystem.specialization.dto.SpecializationResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SpecializationService {

    SpecializationResponseDTO createSpecialization(SpecializationRequestDTO request);

    SpecializationResponseDTO getSpecializationById(Long specializationId);

    Page<SpecializationResponseDTO> getAllSpecializations(Pageable pageable);

    Page<SpecializationResponseDTO> getSpecializationsByDepartment(
            Long departmentId, Pageable pageable);

    SpecializationResponseDTO updateSpecialization(
            Long specializationId, SpecializationRequestDTO request);

    void deleteSpecialization(Long specializationId);
}