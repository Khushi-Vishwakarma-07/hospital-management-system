package com.hospital.management.hospitalmanagementsystem.role.service;

import com.hospital.management.hospitalmanagementsystem.role.dto.RoleRequestDTO;
import com.hospital.management.hospitalmanagementsystem.role.dto.RoleResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleService {

    RoleResponseDTO createRole(RoleRequestDTO dto);

    RoleResponseDTO getRoleById(Long id);

    Page<RoleResponseDTO> getAllRoles(Pageable pageable);

    RoleResponseDTO updateRole(Long id, RoleRequestDTO dto);

    RoleResponseDTO activateRole(Long id);

    RoleResponseDTO deactivateRole(Long id);
}