package com.hospital.management.hospitalmanagementsystem.role.service;

import com.hospital.management.hospitalmanagementsystem.common.exception.DuplicateResourceException;
import com.hospital.management.hospitalmanagementsystem.common.exception.ResourceNotFoundException;
import com.hospital.management.hospitalmanagementsystem.common.util.ConstraintUtils;
import com.hospital.management.hospitalmanagementsystem.role.dto.RoleRequestDTO;
import com.hospital.management.hospitalmanagementsystem.role.dto.RoleResponseDTO;
import com.hospital.management.hospitalmanagementsystem.role.entity.Role;
import com.hospital.management.hospitalmanagementsystem.role.mapper.RoleMapper;
import com.hospital.management.hospitalmanagementsystem.role.repo.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;
    private final RoleMapper mapper;

    @Override
    public RoleResponseDTO createRole(RoleRequestDTO dto) {

        if (repository.existsByNameIgnoreCase(dto.getName())) {
            throw new DuplicateResourceException(
                    "Role already exists with name: " + dto.getName());
        }

        Role role = mapper.toEntity(dto);

        return mapper.toResponse(save(role));
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponseDTO getRoleById(Long id) {
        return mapper.toResponse(getRoleOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RoleResponseDTO> getAllRoles(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    @Override
    public RoleResponseDTO updateRole(Long id, RoleRequestDTO dto) {

        Role role = getRoleForUpdateOrThrow(id);

        if (repository.existsByNameIgnoreCaseAndIdNot(dto.getName(), id)) {
            throw new DuplicateResourceException(
                    "Role already exists with name: " + dto.getName());
        }

        mapper.updateEntity(dto, role);

        return mapper.toResponse(save(role));
    }

    @Override
    public RoleResponseDTO activateRole(Long id) {

        Role role = getRoleForUpdateOrThrow(id);

        if (!role.isActive()) {
            role.setActive(true);
            repository.save(role);
        }

        return mapper.toResponse(role);
    }

    @Override
    public RoleResponseDTO deactivateRole(Long id) {

        Role role = getRoleForUpdateOrThrow(id);

        if (role.isActive()) {
            role.setActive(false);
            repository.save(role);
        }

        return mapper.toResponse(role);
    }

    private Role getRoleOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role not found with ID: " + id));
    }

    private Role getRoleForUpdateOrThrow(Long id) {
        return repository.findByIdForUpdate(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role not found with ID: " + id));
    }

    private Role save(Role role) {
        try {
            return repository.saveAndFlush(role);
        } catch (DataIntegrityViolationException ex) {

            String constraintName = ConstraintUtils.getConstraintName(ex);

            if ("uk_role_name".equals(constraintName)) {
                throw new DuplicateResourceException(
                        "Role already exists with name: " + role.getName()
                );
            }

            throw ex;
        }
    }
}