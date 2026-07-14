package com.hospital.management.hospitalmanagementsystem.doctor.entity;

import com.hospital.management.hospitalmanagementsystem.appointment.entity.Appointment;
import com.hospital.management.hospitalmanagementsystem.common.base.BaseEntity;
import com.hospital.management.hospitalmanagementsystem.schedule.availability.entity.DoctorAvailability;
import com.hospital.management.hospitalmanagementsystem.specialization.entity.Specialization;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "doctors",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_doctor_phone", columnNames = "phone_number"),
                @UniqueConstraint(name = "uk_doctor_email", columnNames = "email")
        },
        indexes = {
                @Index(name = "idx_doctor_specialization", columnList = "specialization_id"),
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor extends BaseEntity {

    @Builder.Default
    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    private List<Appointment> appointments = new ArrayList<>();

    @Builder.Default
    @OneToMany(
            mappedBy = "doctor",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<DoctorAvailability> availabilities = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialization_id", nullable = false)
    private Specialization specialization;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(name = "experience_years", nullable = false)
    private Integer experienceYears;

    @Transient
    public String getFullName() {
        return firstName + " " + lastName;
    }
}