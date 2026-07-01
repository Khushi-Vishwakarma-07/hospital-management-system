package com.hospital.management.hospitalmanagementsystem.specialization.entity;

import com.hospital.management.hospitalmanagementsystem.common.base.BaseEntity;
import com.hospital.management.hospitalmanagementsystem.department.entity.Department;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "specializations",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_specialization_name_department",
                columnNames = {"name", "department_id"}
        ),
        indexes = {
                @Index(
                        name = "idx_specialization_department",
                        columnList = "department_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Specialization extends BaseEntity {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
}