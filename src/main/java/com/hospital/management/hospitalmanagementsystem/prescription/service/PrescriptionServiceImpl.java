package com.hospital.management.hospitalmanagementsystem.prescription.service;

import com.hospital.management.hospitalmanagementsystem.common.exception.DuplicateResourceException;
import com.hospital.management.hospitalmanagementsystem.common.exception.ResourceNotFoundException;
import com.hospital.management.hospitalmanagementsystem.common.util.ConstraintUtils;
import com.hospital.management.hospitalmanagementsystem.medicalrecord.entity.MedicalRecord;
import com.hospital.management.hospitalmanagementsystem.medicalrecord.repository.MedicalRecordRepository;
import com.hospital.management.hospitalmanagementsystem.prescription.dto.PrescriptionRequestDTO;
import com.hospital.management.hospitalmanagementsystem.prescription.dto.PrescriptionResponseDTO;
import com.hospital.management.hospitalmanagementsystem.prescription.dto.PrescriptionUpdateDTO;
import com.hospital.management.hospitalmanagementsystem.prescription.entity.Prescription;
import com.hospital.management.hospitalmanagementsystem.prescription.mapper.PrescriptionMapper;
import com.hospital.management.hospitalmanagementsystem.prescription.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository repository;
    private final MedicalRecordRepository medicalRecordRepository;

    @Transactional
    @Override
    public PrescriptionResponseDTO createPrescription(PrescriptionRequestDTO dto) {
        MedicalRecord medicalRecord = medicalRecordRepository.findByIdForUpdate(dto.getMedicalRecordId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Medical record not found with id: " + dto.getMedicalRecordId()));

        validateUniqueMedicalRecord(dto.getMedicalRecordId());

        Prescription prescription = save(PrescriptionMapper.toEntity(dto, medicalRecord));

        return PrescriptionMapper.toDTO(prescription);
    }

    @Transactional(readOnly = true)
    @Override
    public PrescriptionResponseDTO getPrescriptionById(Long id) {
        return PrescriptionMapper.toDTO(getPrescriptionOrThrow(id));
    }

    @Transactional(readOnly = true)
    @Override
    public PrescriptionResponseDTO getPrescriptionByMedicalRecordId(Long medicalRecordId) {
        Prescription prescription = repository.findByMedicalRecord_Id(medicalRecordId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Prescription not found for medical record id: " + medicalRecordId));
        return PrescriptionMapper.toDTO(prescription);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<PrescriptionResponseDTO> getAllPrescriptions(Pageable pageable) {
        return repository.findAll(pageable)
                .map(PrescriptionMapper::toDTO);
    }

    @Transactional
    @Override
    public PrescriptionResponseDTO updatePrescription(Long id, PrescriptionUpdateDTO dto) {
        Prescription prescription = getPrescriptionForUpdateOrThrow(id);
        PrescriptionMapper.updateEntity(prescription, dto);
        prescription = save(prescription);
        return PrescriptionMapper.toDTO(prescription);
    }

    @Transactional
    @Override
    public void deletePrescription(Long id) {
        Prescription prescription = getPrescriptionForUpdateOrThrow(id);
        repository.delete(prescription);
    }

    // ================= PRIVATE METHODS =================

    private Prescription getPrescriptionOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + id));
    }

    private Prescription getPrescriptionForUpdateOrThrow(Long id) {
        return repository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + id));
    }

    private Prescription save(Prescription prescription) {
        try {
            return repository.saveAndFlush(prescription);
        } catch (DataIntegrityViolationException ex) {
            String constraintName = ConstraintUtils.getConstraintName(ex);

            if ("uk_prescription_medical_record".equals(constraintName)) {
                throw new DuplicateResourceException(
                        "A prescription already exists for medical record id: " + prescription.getMedicalRecord().getId());
            }

            throw ex;
        }
    }

    private void validateUniqueMedicalRecord(Long medicalRecordId) {
        if (repository.existsByMedicalRecord_Id(medicalRecordId)) {
            throw new DuplicateResourceException(
                    "A prescription already exists for medical record id: " + medicalRecordId);
        }
    }
}