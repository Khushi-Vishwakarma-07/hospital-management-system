package com.hospital.management.hospitalmanagementsystem.prescriptionItem.entity;

import com.hospital.management.hospitalmanagementsystem.common.base.BaseEntity;
import com.hospital.management.hospitalmanagementsystem.prescription.entity.Prescription;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "prescription_items",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_prescription_medicine",
                        columnNames = {"prescription_id", "medicine_name"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "prescription_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_prescription_item_prescription")
    )
    private Prescription prescription;

    @Column(name = "medicine_name", nullable = false, length = 200)
    private String medicineName;

    @Column(nullable = false, length = 100)
    private String dosage;

    @Column(nullable = false, length = 100)
    private String frequency;

    @Column(nullable = false, length = 100)
    private String duration;

    @Column(length = 100)
    private String route;

    @Column(columnDefinition = "TEXT")
    private String instructions;
}