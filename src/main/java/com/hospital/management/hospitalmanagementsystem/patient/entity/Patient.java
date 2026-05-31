package com.hospital.management.hospitalmanagementsystem.patient.entity;

import com.hospital.management.hospitalmanagementsystem.common.base.BaseEntity;
import com.hospital.management.hospitalmanagementsystem.patient.enums.BloodGroup;
import com.hospital.management.hospitalmanagementsystem.patient.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "patients",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_patient_phone", columnNames = "phone"),
                @UniqueConstraint(name = "uk_patient_email", columnNames = "email")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Gender gender;

    @Column(nullable = false, length = 15)
    private String phone;

    @Column(length = 150)
    private String email;

    @Column(length = 300)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "blood_group", length = 20, nullable = false)
    private BloodGroup bloodGroup;

    @Column(length = 500)
    private String disease;

    @Column(length = 500)
    private String allergies;

    @Column(name = "emergency_contact_name", length = 100, nullable = false)
    private String emergencyContactName;

    @Column(name = "emergency_contact_phone", length = 15, nullable = false)
    private String emergencyContactPhone;

    @Transient
    public String getFullName() {
        return firstName + " " + lastName;
    }
}