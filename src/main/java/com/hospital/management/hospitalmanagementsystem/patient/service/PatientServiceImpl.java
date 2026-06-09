package com.hospital.management.hospitalmanagementsystem.patient.service;

import com.hospital.management.hospitalmanagementsystem.common.exception.DuplicateResourceException;
import com.hospital.management.hospitalmanagementsystem.common.exception.ResourceNotFoundException;
import com.hospital.management.hospitalmanagementsystem.patient.dto.PatientRequestDTO;
import com.hospital.management.hospitalmanagementsystem.patient.dto.PatientResponseDTO;
import com.hospital.management.hospitalmanagementsystem.patient.entity.Patient;
import com.hospital.management.hospitalmanagementsystem.patient.mapper.PatientMapper;
import com.hospital.management.hospitalmanagementsystem.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository repository;

    @Override
    public PatientResponseDTO createPatient(PatientRequestDTO dto) {

        validateUniqueFields(dto);

        Patient patient = PatientMapper.toEntity(dto);

        Patient saved = repository.save(patient);

        return PatientMapper.toDTO(saved);
    }

    @Override
    public PatientResponseDTO getPatientById(Long id) {

        Patient patient = getPatientOrThrow(id);

        return PatientMapper.toDTO(patient);
    }

    @Override
    public List<PatientResponseDTO> getAllPatients() {

        return repository.findAll()
                .stream()
                .map(PatientMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PatientResponseDTO updatePatient(Long id, PatientRequestDTO dto) {

        Patient patient = getPatientOrThrow(id);

        validateUniqueFieldsForUpdate(patient, dto);

        PatientMapper.updateEntity(patient, dto);

        Patient updated = repository.save(patient);

        return PatientMapper.toDTO(updated);
    }

    @Override
    public void deletePatient(Long id) {

        Patient patient = getPatientOrThrow(id);

        repository.delete(patient);
    }

    // PRIVATE HELPERS

    private Patient getPatientOrThrow(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found with id: " + id
                        ));
    }

    private void validateUniqueFields(PatientRequestDTO dto) {

        if (repository.existsByPhone(dto.getPhone())) {
            throw new DuplicateResourceException(
                    "Phone number already exists"
            );
        }

        if (repository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException(
                    "Email already exists"
            );
        }
    }

    private void validateUniqueFieldsForUpdate(
            Patient patient,
            PatientRequestDTO dto) {

        if (!patient.getEmail().equals(dto.getEmail())
                && repository.existsByEmail(dto.getEmail())) {

            throw new DuplicateResourceException(
                    "Email already exists"
            );
        }

        if (!patient.getPhone().equals(dto.getPhone())
                && repository.existsByPhone(dto.getPhone())) {

            throw new DuplicateResourceException(
                    "Phone number already exists"
            );
        }
    }
}