package com.hospital.management.hospitalmanagementsystem.user.mapper;

import com.hospital.management.hospitalmanagementsystem.role.entity.Role;
import com.hospital.management.hospitalmanagementsystem.user.dto.UserRequestDTO;
import com.hospital.management.hospitalmanagementsystem.user.dto.UserResponseDTO;
import com.hospital.management.hospitalmanagementsystem.user.dto.UserUpdateDTO;
import com.hospital.management.hospitalmanagementsystem.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public User toEntity(UserRequestDTO request, Role role) {

        return User.builder()
                .firstName(request.getFirstName().trim())
                .lastName(request.getLastName().trim())
                .email(request.getEmail().trim().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .active(true)
                .build();
    }

    public void updateEntity(User user, UserUpdateDTO request, Role role) {

        user.setFirstName(request.getFirstName().trim());
        user.setLastName(request.getLastName().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setRole(role);

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
    }

    public UserResponseDTO toResponse(User user) {

        return UserResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .roleId(user.getRole().getId())
                .roleName(user.getRole().getName())
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}