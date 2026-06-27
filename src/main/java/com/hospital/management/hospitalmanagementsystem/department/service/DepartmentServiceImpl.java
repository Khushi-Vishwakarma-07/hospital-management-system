package com.hospital.management.hospitalmanagementsystem.department.service;

import com.hospital.management.hospitalmanagementsystem.common.exception.DuplicateResourceException;
import com.hospital.management.hospitalmanagementsystem.common.exception.ResourceNotFoundException;
import com.hospital.management.hospitalmanagementsystem.department.dto.DepartmentRequestDTO;
import com.hospital.management.hospitalmanagementsystem.department.dto.DepartmentResponseDTO;
import com.hospital.management.hospitalmanagementsystem.department.entity.Department;
import com.hospital.management.hospitalmanagementsystem.department.mapper.DepartmentMapper;
import com.hospital.management.hospitalmanagementsystem.department.repository.DepartmentRepository;
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

    @Transactional
    @Override
    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO dto) {
        validateUniqueName(dto.getName(), null);

        return DepartmentMapper.toDTO(
                save(
                        DepartmentMapper.toEntity(dto)
                )
        );
    }

    @Transactional(readOnly = true)
    @Override
    public DepartmentResponseDTO getDepartmentById(Long id) {
        return DepartmentMapper.toDTO(
                getDepartmentOrThrow(id)
        );
    }

    @Transactional(readOnly = true)
    @Override
    public Page<DepartmentResponseDTO> getAllDepartments(Pageable pageable) {
        return repository.findAll(pageable)
                .map(DepartmentMapper::toDTO);
    }

    @Transactional
    @Override
    public DepartmentResponseDTO updateDepartment(Long id,
                                                  DepartmentRequestDTO dto) {

        Department department = getDepartmentOrThrow(id);

        validateUniqueName(dto.getName(), id);

        DepartmentMapper.updateEntity(department, dto);

        return DepartmentMapper.toDTO(
                save(department)
        );
    }

    @Transactional
    @Override
    public void deleteDepartment(Long id) {
        Department department = getDepartmentOrThrow(id);
        repository.delete(department);
    }

    // -------------------------------------------------------------------------

    private Department getDepartmentOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Department not found with id: " + id
                        ));
    }

    private Department save(Department department) {
        try {
            return repository.save(department);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateResourceException(
                    "Department already exists: " + department.getName()
            );
        }
    }

    private void validateUniqueName(String name, Long currentId) {

        boolean exists = currentId == null
                ? repository.existsByNameIgnoreCase(name)
                : repository.existsByNameIgnoreCaseAndIdNot(name, currentId);

        if (exists) {
            throw new DuplicateResourceException(
                    "Department already exists: " + name
            );
        }
    }
}