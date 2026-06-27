package com.hospital.management.hospitalmanagementsystem.department.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentResponseDTO {

    private Long id;

    private String name;

    private String description;
}