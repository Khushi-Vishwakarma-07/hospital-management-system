package com.hospital.management.hospitalmanagementsystem.role.mapper;

import com.hospital.management.hospitalmanagementsystem.role.dto.RoleRequestDTO;
import com.hospital.management.hospitalmanagementsystem.role.dto.RoleResponseDTO;
import com.hospital.management.hospitalmanagementsystem.role.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public Role toEntity(RoleRequestDTO dto) {
        return Role.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

    public RoleResponseDTO toResponse(Role role) {
        return RoleResponseDTO.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .active(role.isActive())
                .createdAt(role.getCreatedAt())
                .updatedAt(role.getUpdatedAt())
                .build();
    }

    public void updateEntity(RoleRequestDTO dto, Role role) {
        role.setName(dto.getName());
        role.setDescription(dto.getDescription());
    }
}