package com.hospital.management.hospitalmanagementsystem.user.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private Long roleId;

    private String roleName;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}