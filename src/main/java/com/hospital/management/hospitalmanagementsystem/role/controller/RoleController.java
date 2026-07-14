package com.hospital.management.hospitalmanagementsystem.role.controller;

import com.hospital.management.hospitalmanagementsystem.role.dto.RoleRequestDTO;
import com.hospital.management.hospitalmanagementsystem.role.dto.RoleResponseDTO;
import com.hospital.management.hospitalmanagementsystem.role.service.RoleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Validated
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<RoleResponseDTO> create(
            @Valid @RequestBody RoleRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roleService.createRole(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> getById(
            @PathVariable
            @Positive(message = "Role ID must be a positive number") Long id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @GetMapping
    public ResponseEntity<Page<RoleResponseDTO>> getAll(
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(roleService.getAllRoles(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> update(
            @PathVariable
            @Positive(message = "Role ID must be a positive number") Long id,
            @Valid @RequestBody RoleRequestDTO dto) {
        return ResponseEntity.ok(roleService.updateRole(id, dto));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<RoleResponseDTO> activate(
            @PathVariable
            @Positive(message = "Role ID must be a positive number") Long id) {
        return ResponseEntity.ok(roleService.activateRole(id));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<RoleResponseDTO> deactivate(
            @PathVariable
            @Positive(message = "Role ID must be a positive number") Long id) {
        return ResponseEntity.ok(roleService.deactivateRole(id));
    }
}