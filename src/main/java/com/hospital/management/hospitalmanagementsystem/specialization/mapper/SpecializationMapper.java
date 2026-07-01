package com.hospital.management.hospitalmanagementsystem.specialization.mapper;

import com.hospital.management.hospitalmanagementsystem.department.entity.Department;
import com.hospital.management.hospitalmanagementsystem.specialization.dto.SpecializationRequestDTO;
import com.hospital.management.hospitalmanagementsystem.specialization.dto.SpecializationResponseDTO;
import com.hospital.management.hospitalmanagementsystem.specialization.entity.Specialization;

public final class SpecializationMapper {

    private SpecializationMapper() {}

    public static Specialization toEntity(
            SpecializationRequestDTO dto,
            Department department) {

        return Specialization.builder()
                .name(dto.getSpecializationName())
                .description(dto.getSpecializationDescription())
                .department(department)
                .build();
    }

    public static SpecializationResponseDTO toResponse(Specialization specialization) {
        return SpecializationResponseDTO.builder()
                .specializationId(specialization.getId())
                .specializationName(specialization.getName())
                .specializationDescription(specialization.getDescription())
                .departmentId(specialization.getDepartment().getId())
                .departmentName(specialization.getDepartment().getName())
                .createdAt(specialization.getCreatedAt())
                .updatedAt(specialization.getUpdatedAt())
                .build();
    }

    public static void updateEntity(
            Specialization specialization,
            SpecializationRequestDTO dto,
            Department department) {

        specialization.setName(dto.getSpecializationName());
        specialization.setDescription(dto.getSpecializationDescription());
        specialization.setDepartment(department);
    }
}