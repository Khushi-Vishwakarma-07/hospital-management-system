package com.hospital.management.hospitalmanagementsystem.department.service;

import com.hospital.management.hospitalmanagementsystem.common.exception.BusinessException;
import com.hospital.management.hospitalmanagementsystem.common.exception.DuplicateResourceException;
import com.hospital.management.hospitalmanagementsystem.common.exception.ResourceNotFoundException;
import com.hospital.management.hospitalmanagementsystem.department.dto.DepartmentRequestDTO;
import com.hospital.management.hospitalmanagementsystem.department.dto.DepartmentResponseDTO;
import com.hospital.management.hospitalmanagementsystem.department.entity.Department;
import com.hospital.management.hospitalmanagementsystem.department.mapper.DepartmentMapper;
import com.hospital.management.hospitalmanagementsystem.department.repository.DepartmentRepository;
import com.hospital.management.hospitalmanagementsystem.specialization.repository.SpecializationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository repository;
    private final SpecializationRepository specializationRepository;

    @Transactional
    @Override
    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO dto) {

        if (repository.existsByNameIgnoreCase(dto.getName())) {
            throw new DuplicateResourceException(
                    "A department with that name already exists"
            );
        }

        return DepartmentMapper.toDTO(
                save(DepartmentMapper.toEntity(dto))
        );
    }

    @Transactional(readOnly = true)
    @Override
    public DepartmentResponseDTO getDepartmentById(Long id) {
        return DepartmentMapper.toDTO(getDepartmentOrThrow(id));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<DepartmentResponseDTO> getAllDepartments(Pageable pageable) {
        return repository.findAll(pageable).map(DepartmentMapper::toDTO);
    }

    @Transactional
    @Override
    public DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO dto) {

        Department department = getDepartmentForUpdateOrThrow(id);

        if (repository.existsByNameIgnoreCaseAndIdNot(dto.getName(), id)) {
            throw new DuplicateResourceException(
                    "A department with that name already exists"
            );
        }

        DepartmentMapper.updateEntity(department, dto);
        return DepartmentMapper.toDTO(save(department));
    }

    @Transactional
    @Override
    public void deleteDepartment(Long id) {

        Department department = getDepartmentForUpdateOrThrow(id);

        if (specializationRepository.existsByDepartmentId(id)) {
            throw new BusinessException(
                    "Cannot delete department: it still has specializations assigned to it"
            );
        }

        repository.delete(department);
    }

    // ---------------- HELPERS ----------------

    private Department getDepartmentOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department not found"   // id omitted from message intentionally
                ));
    }

    private Department getDepartmentForUpdateOrThrow(Long id) {
        return repository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Department not found"
                ));
    }

    private Department save(Department department) {
        try {
            return repository.saveAndFlush(department);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateResourceException(
                    "A department with that name already exists"
            );
        }
    }
}