package com.hospital.management.hospitalmanagementsystem.doctor.mapper;

import com.hospital.management.hospitalmanagementsystem.doctor.dto.DoctorRequestDTO;
import com.hospital.management.hospitalmanagementsystem.doctor.dto.DoctorResponseDTO;
import com.hospital.management.hospitalmanagementsystem.doctor.entity.Doctor;
import com.hospital.management.hospitalmanagementsystem.specialization.entity.Specialization;

public final class DoctorMapper {

    private DoctorMapper() {
    }

    public static Doctor toEntity(
            DoctorRequestDTO dto,
            Specialization specialization) {

        return Doctor.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .specialization(specialization)
                .phoneNumber(dto.getPhoneNumber())
                .email(dto.getEmail())
                .experienceYears(dto.getExperienceYears())
                .build();
    }

    public static DoctorResponseDTO toDTO(Doctor doctor) {

        return DoctorResponseDTO.builder()
                .id(doctor.getId())
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .fullName(doctor.getFullName())
                .specializationId(doctor.getSpecialization().getId())
                .specializationName(doctor.getSpecialization().getName())
                .phoneNumber(doctor.getPhoneNumber())
                .email(doctor.getEmail())
                .experienceYears(doctor.getExperienceYears())
                .createdAt(doctor.getCreatedAt())
                .updatedAt(doctor.getUpdatedAt())
                .build();
    }

    public static void updateEntity(
            Doctor doctor,
            DoctorRequestDTO dto,
            Specialization specialization) {

        doctor.setFirstName(dto.getFirstName());
        doctor.setLastName(dto.getLastName());
        doctor.setSpecialization(specialization);
        doctor.setPhoneNumber(dto.getPhoneNumber());
        doctor.setEmail(dto.getEmail());
        doctor.setExperienceYears(dto.getExperienceYears());
    }
}