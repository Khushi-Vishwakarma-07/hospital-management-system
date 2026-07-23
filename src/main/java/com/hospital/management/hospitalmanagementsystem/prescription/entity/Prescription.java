package com.hospital.management.hospitalmanagementsystem.prescription.entity;

import com.hospital.management.hospitalmanagementsystem.common.base.BaseEntity;
import com.hospital.management.hospitalmanagementsystem.medicalrecord.entity.MedicalRecord;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "prescriptions",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_prescription_medical_record",
                        columnNames = "medical_record_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prescription extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "medical_record_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_prescription_medical_record")
    )
    private MedicalRecord medicalRecord;

    @Column(name = "medication_instructions", columnDefinition = "TEXT")
    private String medicationInstructions;

    @Column(name = "general_instructions", columnDefinition = "TEXT")
    private String generalInstructions;
}