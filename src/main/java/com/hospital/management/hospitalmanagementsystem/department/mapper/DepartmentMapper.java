package com.hospital.management.hospitalmanagementsystem.department.mapper;

import com.hospital.management.hospitalmanagementsystem.department.dto.DepartmentRequestDTO;
import com.hospital.management.hospitalmanagementsystem.department.dto.DepartmentResponseDTO;
import com.hospital.management.hospitalmanagementsystem.department.entity.Department;

public final class DepartmentMapper {

    private DepartmentMapper() {}

    public static Department toEntity(DepartmentRequestDTO dto) {
        return Department.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

    public static DepartmentResponseDTO toDTO(Department department) {
        return DepartmentResponseDTO.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .createdAt(department.getCreatedAt())
                .updatedAt(department.getUpdatedAt())
                .build();
    }

    public static void updateEntity(
            Department department,
            DepartmentRequestDTO dto) {

        department.setName(dto.getName());
        department.setDescription(dto.getDescription());
    }
}