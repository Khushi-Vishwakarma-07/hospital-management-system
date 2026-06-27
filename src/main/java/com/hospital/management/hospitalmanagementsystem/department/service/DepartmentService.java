package com.hospital.management.hospitalmanagementsystem.department.service;

import com.hospital.management.hospitalmanagementsystem.department.dto.DepartmentRequestDTO;
import com.hospital.management.hospitalmanagementsystem.department.dto.DepartmentResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DepartmentService {

    DepartmentResponseDTO createDepartment(DepartmentRequestDTO requestDTO);

    DepartmentResponseDTO getDepartmentById(Long id);

    Page<DepartmentResponseDTO> getAllDepartments(Pageable pageable);

    DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO requestDTO);

    void deleteDepartment(Long id);
}