package com.hospital.management.hospitalmanagementsystem.user.service;

import com.hospital.management.hospitalmanagementsystem.user.dto.UserRequestDTO;
import com.hospital.management.hospitalmanagementsystem.user.dto.UserResponseDTO;
import com.hospital.management.hospitalmanagementsystem.user.dto.UserUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserResponseDTO create(UserRequestDTO request);

    UserResponseDTO update(Long id, UserUpdateDTO request);

    UserResponseDTO getById(Long id);

    Page<UserResponseDTO> getAll(Pageable pageable);

    UserResponseDTO deactivate(Long id);

    UserResponseDTO activate(Long id);
}