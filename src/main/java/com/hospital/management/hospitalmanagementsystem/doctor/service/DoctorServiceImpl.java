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

        return DoctorMapper.toDTO(getDoctorOrThrow(id));
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

        Doctor doctor = getDoctorOrThrow(id);

        validateUniqueFields(dto.getEmail(), dto.getPhoneNumber(), id);

        DoctorMapper.updateEntity(doctor, dto);

        Doctor updated = repository.save(doctor);

        return DoctorMapper.toDTO(updated);
    }

    @Override
    public void deleteDoctor(Long id) {

        Doctor doctor = getDoctorOrThrow(id);

        repository.delete(doctor);
    }

    private Doctor getDoctorOrThrow(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with id: " + id));
    }

    private void validateOwnership(Long currentId,
                                   Long existingId,
                                   String message) {

        if (currentId == null || !currentId.equals(existingId)) {
            throw new BusinessException(message);
        }
    }

    private void validateUniqueFields(String email,
                                      String phoneNumber,
                                      Long currentId) {

        repository.findByEmail(email).ifPresent(doctor ->
                validateOwnership(
                        currentId,
                        doctor.getId(),
                        "Email already exists"
                )
        );

        repository.findByPhoneNumber(phoneNumber).ifPresent(doctor ->
                validateOwnership(
                        currentId,
                        doctor.getId(),
                        "Phone number already exists"
                )
        );
    }
}