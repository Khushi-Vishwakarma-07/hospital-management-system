package com.hospital.management.hospitalmanagementsystem.patient.mapper;

import com.hospital.management.hospitalmanagementsystem.patient.dto.PatientRequestDTO;
import com.hospital.management.hospitalmanagementsystem.patient.dto.PatientResponseDTO;
import com.hospital.management.hospitalmanagementsystem.patient.entity.Patient;

public final class PatientMapper {

    private PatientMapper() {
    }

    // DTO -> ENTITY
    public static Patient toEntity(PatientRequestDTO dto) {

        return Patient.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .dateOfBirth(dto.getDateOfBirth())
                .gender(dto.getGender())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .address(dto.getAddress())
                .bloodGroup(dto.getBloodGroup())
                .disease(dto.getDisease())
                .allergies(dto.getAllergies())
                .emergencyContactName(dto.getEmergencyContactName())
                .emergencyContactPhone(dto.getEmergencyContactPhone())
                .build();
    }

    // ENTITY -> DTO
    public static PatientResponseDTO toDTO(Patient patient) {

        return PatientResponseDTO.builder()
                .id(patient.getId())

                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())

                .fullName(patient.getFullName())
                .age(patient.getAge())

                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender())

                .phone(patient.getPhone())
                .email(patient.getEmail())
                .address(patient.getAddress())

                .bloodGroup(patient.getBloodGroup())

                .disease(patient.getDisease())
                .allergies(patient.getAllergies())

                .emergencyContactName(patient.getEmergencyContactName())
                .emergencyContactPhone(patient.getEmergencyContactPhone())

                .createdAt(patient.getCreatedAt())
                .updatedAt(patient.getUpdatedAt())

                .build();
    }

    // UPDATE EXISTING ENTITY
    public static void updateEntity(Patient patient, PatientRequestDTO dto) {

        patient.setFirstName(dto.getFirstName());
        patient.setLastName(dto.getLastName());
        patient.setDateOfBirth(dto.getDateOfBirth());
        patient.setGender(dto.getGender());
        patient.setPhone(dto.getPhone());
        patient.setEmail(dto.getEmail());
        patient.setAddress(dto.getAddress());
        patient.setBloodGroup(dto.getBloodGroup());
        patient.setDisease(dto.getDisease());
        patient.setAllergies(dto.getAllergies());
        patient.setEmergencyContactName(dto.getEmergencyContactName());
        patient.setEmergencyContactPhone(dto.getEmergencyContactPhone());
    }
}