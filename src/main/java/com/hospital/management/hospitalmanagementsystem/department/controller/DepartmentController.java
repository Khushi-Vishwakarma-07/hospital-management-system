package com.hospital.management.hospitalmanagementsystem.department.controller;

import com.hospital.management.hospitalmanagementsystem.department.dto.DepartmentRequestDTO;
import com.hospital.management.hospitalmanagementsystem.department.dto.DepartmentResponseDTO;
import com.hospital.management.hospitalmanagementsystem.department.service.DepartmentService;
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
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@Validated
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<DepartmentResponseDTO> create(
            @Valid @RequestBody DepartmentRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(departmentService.createDepartment(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> getById(
            @PathVariable
            @Positive(message = "Department ID must be a positive number")
            Long id) {

        return ResponseEntity.ok(
                departmentService.getDepartmentById(id)
        );
    }

    @GetMapping
    public ResponseEntity<Page<DepartmentResponseDTO>> getAll(
            @PageableDefault(size = 20, sort = "name")
            Pageable pageable) {

        return ResponseEntity.ok(
                departmentService.getAllDepartments(pageable)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> update(
            @PathVariable
            @Positive(message = "Department ID must be a positive number")
            Long id,

            @Valid @RequestBody DepartmentRequestDTO dto) {

        return ResponseEntity.ok(
                departmentService.updateDepartment(id, dto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable
            @Positive(message = "Department ID must be a positive number")
            Long id) {

        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}