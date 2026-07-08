package com.hospital.management.hospitalmanagementsystem.shedule.leave.controller;

import com.hospital.management.hospitalmanagementsystem.shedule.leave.dto.*;
import com.hospital.management.hospitalmanagementsystem.shedule.leave.service.DoctorLeaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctor-leaves")
@RequiredArgsConstructor
@Validated
public class DoctorLeaveController {

    private final DoctorLeaveService doctorLeaveService;

    @PostMapping
    public ResponseEntity<DoctorLeaveResponseDTO> createLeave(
            @Valid @RequestBody DoctorLeaveRequestDTO requestDTO
    ) {
        DoctorLeaveResponseDTO response = doctorLeaveService.createLeave(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{leaveId}")
    public ResponseEntity<DoctorLeaveResponseDTO> getLeaveById(
            @PathVariable Long leaveId
    ) {
        return ResponseEntity.ok(doctorLeaveService.getLeaveById(leaveId));
    }

    @GetMapping
    public ResponseEntity<Page<DoctorLeaveResponseDTO>> getAllLeaves(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(doctorLeaveService.getAllLeaves(pageable));
    }

    @PutMapping("/{leaveId}")
    public ResponseEntity<DoctorLeaveResponseDTO> updateLeave(
            @PathVariable Long leaveId,
            @Valid @RequestBody DoctorLeaveUpdateDTO requestDTO
    ) {
        return ResponseEntity.ok(doctorLeaveService.updateLeave(leaveId, requestDTO));
    }

    @PatchMapping("/{leaveId}/status")
    public ResponseEntity<DoctorLeaveResponseDTO> updateLeaveStatus(
            @PathVariable Long leaveId,
            @Valid @RequestBody DoctorLeaveStatusUpdateDTO requestDTO
    ) {
        return ResponseEntity.ok(doctorLeaveService.updateLeaveStatus(leaveId, requestDTO));
    }

    @DeleteMapping("/{leaveId}")
    public ResponseEntity<Void> deleteLeave(
            @PathVariable Long leaveId
    ) {
        doctorLeaveService.deleteLeave(leaveId);
        return ResponseEntity.noContent().build();
    }
}