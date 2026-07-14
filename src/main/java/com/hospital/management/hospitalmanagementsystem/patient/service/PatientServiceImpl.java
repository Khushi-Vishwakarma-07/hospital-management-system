package com.hospital.management.hospitalmanagementsystem.patient.service;

import com.hospital.management.hospitalmanagementsystem.appointment.repository.AppointmentRepository;
import com.hospital.management.hospitalmanagementsystem.common.exception.BusinessException;
import com.hospital.management.hospitalmanagementsystem.common.exception.DuplicateResourceException;
import com.hospital.management.hospitalmanagementsystem.common.exception.ResourceNotFoundException;
import com.hospital.management.hospitalmanagementsystem.common.util.ConstraintUtils;
import com.hospital.management.hospitalmanagementsystem.patient.dto.PatientRequestDTO;
import com.hospital.management.hospitalmanagementsystem.patient.dto.PatientResponseDTO;
import com.hospital.management.hospitalmanagementsystem.patient.entity.Patient;
import com.hospital.management.hospitalmanagementsystem.patient.mapper.PatientMapper;
import com.hospital.management.hospitalmanagementsystem.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository repository;
    private final AppointmentRepository appointmentRepository;

    @Transactional
    @Override
    public PatientResponseDTO createPatient(PatientRequestDTO dto) {
        validateUniqueFields(dto.getEmail(), dto.getPhone());
        return PatientMapper.toDTO(save(PatientMapper.toEntity(dto)));
    }

    @Transactional(readOnly = true)
    @Override
    public PatientResponseDTO getPatientById(Long id) {
        return PatientMapper.toDTO(getPatientOrThrow(id));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<PatientResponseDTO> getAllPatients(Pageable pageable) {
        return repository.findAll(pageable).map(PatientMapper::toDTO);
    }

    @Transactional
    @Override
    public PatientResponseDTO updatePatient(Long id, PatientRequestDTO dto) {
        Patient patient = getPatientForUpdateOrThrow(id);
        validateUniqueFieldsForUpdate(patient, dto);
        PatientMapper.updateEntity(patient, dto);
        return PatientMapper.toDTO(save(patient));
    }

    @Transactional
    @Override
    public void deletePatient(Long id) {

        Patient patient = getPatientForUpdateOrThrow(id);

        if (appointmentRepository.existsByPatient_Id(id)) {
            throw new BusinessException(
                    "Cannot delete patient with existing appointments");
        }

        repository.delete(patient);
    }

    // ================= PRIVATE METHODS =================

    private Patient getPatientOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
    }

    private Patient getPatientForUpdateOrThrow(Long id) {
        return repository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
    }

    private Patient save(Patient patient) {

        try {
            return repository.saveAndFlush(patient);

        } catch (DataIntegrityViolationException ex) {

            String constraintName = ConstraintUtils.getConstraintName(ex);

            if ("uk_patient_phone".equals(constraintName)) {
                throw new DuplicateResourceException(
                        "A patient with that phone number already exists"
                );
            }

            if ("uk_patient_email".equals(constraintName)) {
                throw new DuplicateResourceException(
                        "A patient with that email already exists"
                );
            }

            throw ex;
        }
    }

    private void validateUniqueFields(String email, String phone) {
        if (repository.existsByEmail(email)) throw new DuplicateResourceException("Email already exists: " + email);
        if (repository.existsByPhone(phone)) throw new DuplicateResourceException("Phone already exists: " + phone);
    }

    private void validateUniqueFieldsForUpdate(Patient existing, PatientRequestDTO dto) {
        if (!existing.getEmail().equals(dto.getEmail()) && repository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + dto.getEmail());
        }
        if (!existing.getPhone().equals(dto.getPhone()) && repository.existsByPhone(dto.getPhone())) {
            throw new DuplicateResourceException("Phone already exists: " + dto.getPhone());
        }
    }
}