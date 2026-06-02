package com.hospital.management.hospitalmanagementsystem.doctor.service;

import com.hospital.management.hospitalmanagementsystem.common.exception.BusinessException;
import com.hospital.management.hospitalmanagementsystem.common.exception.ResourceNotFoundException;
import com.hospital.management.hospitalmanagementsystem.doctor.dto.DoctorRequestDTO;
import com.hospital.management.hospitalmanagementsystem.doctor.dto.DoctorResponseDTO;
import com.hospital.management.hospitalmanagementsystem.doctor.entity.Doctor;
import com.hospital.management.hospitalmanagementsystem.doctor.mapper.DoctorMapper;
import com.hospital.management.hospitalmanagementsystem.doctor.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository repository;

    @Override
    public DoctorResponseDTO createDoctor(DoctorRequestDTO dto) {

        validateUniqueFields(dto.getEmail(), dto.getPhoneNumber(), null);

        Doctor doctor = DoctorMapper.toEntity(dto);
        Doctor savedDoctor = repository.save(doctor);

        return DoctorMapper.toDTO(savedDoctor);
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorResponseDTO getDoctorById(Long id) {

        Doctor doctor = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor not found with id: " + id));

        return DoctorMapper.toDTO(doctor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorResponseDTO> getAllDoctors() {

        return repository.findAll()
                .stream()
                .map(DoctorMapper::toDTO)
                .toList();
    }

    @Override
    public DoctorResponseDTO updateDoctor(Long id, DoctorRequestDTO dto) {

        Doctor doctor = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Doctor not found with id: " + id));

        validateUniqueFields(dto.getEmail(), dto.getPhoneNumber(), id);

        DoctorMapper.updateEntity(doctor, dto);

        Doctor updated = repository.save(doctor);

        return DoctorMapper.toDTO(updated);
    }

    @Override
    public void deleteDoctor(Long id) {

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Doctor not found with id: " + id);
        }

        repository.deleteById(id);
    }

//    /**
//     * Validates that email and phone number remain unique.
//     *
//     * CREATE (currentId == null):
//     * ---------------------------
//     * - If email already exists -> throw exception.
//     * - If phone number already exists -> throw exception.
//     *
//     * UPDATE (currentId != null):
//     * ---------------------------
//     * 1. If email does not exist in database:
//     *      -> safe to update.
//     *
//     * 2. If email exists and belongs to the same doctor:
//     *      -> safe to update.
//     *      Example:
//     *      currentId = 2
//     *      email belongs to doctor with id = 2
//     *
//     * 3. If email exists and belongs to another doctor:
//     *      -> throw exception.
//     *      Example:
//     *      currentId = 1
//     *      email belongs to doctor with id = 3
//     *
//     * Same logic applies to phone number.
//     *
//     * Purpose:
//     * Prevent duplicate emails and phone numbers before saving,
//     * while still allowing a doctor to keep their own existing
//     * email/phone during updates.
//     */

    private void validateUniqueFields(String email, String phoneNumber, Long currentId) {

        Doctor doctorWithEmail = repository.findByEmail(email).orElse(null);

        if (doctorWithEmail != null &&
                (currentId == null ||
                        !doctorWithEmail.getId().equals(currentId))) {

            throw new BusinessException("Email already exists");
        }

        Doctor doctorWithPhoneNumber =
                repository.findByPhoneNumber(phoneNumber).orElse(null);

        if (doctorWithPhoneNumber != null &&
                (currentId == null ||
                        !doctorWithPhoneNumber.getId().equals(currentId))) {

            throw new BusinessException("Phone number already exists");
        }
    }
}