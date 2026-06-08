package com.hospital.management.hospitalmanagementsystem.patient.entity;

import com.hospital.management.hospitalmanagementsystem.appointment.entity.Appointment;
import com.hospital.management.hospitalmanagementsystem.common.base.BaseEntity;
import com.hospital.management.hospitalmanagementsystem.patient.enums.BloodGroup;
import com.hospital.management.hospitalmanagementsystem.patient.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

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

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    private List<Appointment> appointments;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Gender gender;

    @Column(nullable = false, length = 15)
    private String phone;

    @Column(nullable = false, length = 150)
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

    @Transient
    public Integer getAge() {
        if (dateOfBirth == null) return null;
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }
}