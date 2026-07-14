package com.hospital.management.hospitalmanagementsystem.specialization.service;

import com.hospital.management.hospitalmanagementsystem.common.exception.BusinessException;
import com.hospital.management.hospitalmanagementsystem.common.exception.DuplicateResourceException;
import com.hospital.management.hospitalmanagementsystem.common.exception.ResourceNotFoundException;
import com.hospital.management.hospitalmanagementsystem.department.entity.Department;
import com.hospital.management.hospitalmanagementsystem.department.repository.DepartmentRepository;
import com.hospital.management.hospitalmanagementsystem.doctor.repository.DoctorRepository;
import com.hospital.management.hospitalmanagementsystem.specialization.dto.SpecializationRequestDTO;
import com.hospital.management.hospitalmanagementsystem.specialization.dto.SpecializationResponseDTO;
import com.hospital.management.hospitalmanagementsystem.specialization.entity.Specialization;
import com.hospital.management.hospitalmanagementsystem.specialization.mapper.SpecializationMapper;
import com.hospital.management.hospitalmanagementsystem.specialization.repository.SpecializationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SpecializationServiceImpl implements SpecializationService {

    private final SpecializationRepository repository;
    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;

    @Transactional
    @Override
    public SpecializationResponseDTO createSpecialization(SpecializationRequestDTO dto) {

        Department department = getDepartmentForUpdateOrThrow(dto.getDepartmentId());

        if (repository.existsByNameIgnoreCaseAndDepartmentId(
                dto.getSpecializationName(),
                dto.getDepartmentId())) {

            throw new DuplicateResourceException(
                    "A specialization with that name already exists in this department"
            );
        }

        Specialization specialization =
                SpecializationMapper.toEntity(dto, department);

        return SpecializationMapper.toResponse(save(specialization));
    }

    @Transactional(readOnly = true)
    @Override
    public SpecializationResponseDTO getSpecializationById(Long id) {
        return SpecializationMapper.toResponse(getSpecializationWithDepartmentOrThrow(id));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<SpecializationResponseDTO> getAllSpecializations(Pageable pageable) {
        return repository.findAllWithDepartment(pageable)
                .map(SpecializationMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<SpecializationResponseDTO> getSpecializationsByDepartment(
            Long departmentId,
            Pageable pageable) {

        validateDepartmentExists(departmentId);

        return repository.findByDepartmentId(departmentId, pageable)
                .map(SpecializationMapper::toResponse);
    }

    @Transactional
    @Override
    public SpecializationResponseDTO updateSpecialization(
            Long id,
            SpecializationRequestDTO dto) {

        Specialization specialization = getSpecializationForUpdateOrThrow(id);

        Department department =
                specialization.getDepartment().getId().equals(dto.getDepartmentId())
                        ? specialization.getDepartment()
                        : getDepartmentForUpdateOrThrow(dto.getDepartmentId());

        if (repository.existsByNameIgnoreCaseAndDepartmentIdAndIdNot(
                dto.getSpecializationName(),
                dto.getDepartmentId(),
                id)) {

            throw new DuplicateResourceException(
                    "A specialization with that name already exists in this department"
            );
        }

        SpecializationMapper.updateEntity(
                specialization,
                dto,
                department
        );

        return SpecializationMapper.toResponse(save(specialization));
    }

    @Transactional
    @Override
    public void deleteSpecialization(Long id) {

        Specialization specialization = getSpecializationForUpdateOrThrow(id);

        if (doctorRepository.existsBySpecialization_Id(id)) {
            throw new BusinessException(
                    "Cannot delete specialization: it still has doctors assigned to it"
            );
        }

        repository.delete(specialization);
    }

    // ---------------- HELPERS ----------------

    private Department getDepartmentForUpdateOrThrow(Long id) {
        return departmentRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department not found"
                ));
    }

    private void validateDepartmentExists(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Department not found");
        }
    }

    private Specialization getSpecializationWithDepartmentOrThrow(Long id) {
        return repository.findWithDepartmentById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Specialization not found"
                ));
    }

    private Specialization getSpecializationForUpdateOrThrow(Long id) {
        return repository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Specialization not found"
                ));
    }

    private Specialization save(Specialization specialization) {
        try {
            return repository.saveAndFlush(specialization);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateResourceException(
                    "A specialization with that name already exists in this department"
            );
        }
    }
}