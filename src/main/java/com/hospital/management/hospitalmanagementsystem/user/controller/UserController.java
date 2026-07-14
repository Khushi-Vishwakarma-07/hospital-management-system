package com.hospital.management.hospitalmanagementsystem.user.controller;

import com.hospital.management.hospitalmanagementsystem.user.dto.UserRequestDTO;
import com.hospital.management.hospitalmanagementsystem.user.dto.UserResponseDTO;
import com.hospital.management.hospitalmanagementsystem.user.dto.UserUpdateDTO;
import com.hospital.management.hospitalmanagementsystem.user.service.UserService;
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
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(
            @Valid @RequestBody UserRequestDTO request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getById(
            @PathVariable
            @Positive(message = "User ID must be a positive number")
            Long id) {

        return ResponseEntity.ok(userService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> getAll(
            @PageableDefault(size = 20, sort = "firstName")
            Pageable pageable) {

        return ResponseEntity.ok(userService.getAll(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(
            @PathVariable
            @Positive(message = "User ID must be a positive number")
            Long id,

            @Valid @RequestBody UserUpdateDTO request) {

        return ResponseEntity.ok(userService.update(id, request));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<UserResponseDTO> activate(
            @PathVariable
            @Positive(message = "User ID must be a positive number")
            Long id) {

        return ResponseEntity.ok(userService.activate(id));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<UserResponseDTO> deactivate(
            @PathVariable
            @Positive(message = "User ID must be a positive number")
            Long id) {

        return ResponseEntity.ok(userService.deactivate(id));
    }
}