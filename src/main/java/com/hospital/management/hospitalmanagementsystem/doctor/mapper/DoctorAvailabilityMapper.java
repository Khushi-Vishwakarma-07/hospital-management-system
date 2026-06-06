package com.hospital.management.hospitalmanagementsystem.doctor.mapper;

import com.hospital.management.hospitalmanagementsystem.doctor.dto.AvailabilityRequestDTO;
import com.hospital.management.hospitalmanagementsystem.doctor.dto.AvailabilityResponseDTO;
import com.hospital.management.hospitalmanagementsystem.doctor.entity.DoctorAvailability;

public final class DoctorAvailabilityMapper {

    private DoctorAvailabilityMapper() {
    }

    public static DoctorAvailability toEntity(
            AvailabilityRequestDTO dto) {

        return DoctorAvailability.builder()
                .dayOfWeek(dto.getDayOfWeek())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .build();
    }

    public static AvailabilityResponseDTO toDTO(
            DoctorAvailability availability) {

        return AvailabilityResponseDTO.builder()
                .id(availability.getId())
                .doctorId(availability.getDoctor().getId())
                .dayOfWeek(availability.getDayOfWeek())
                .startTime(availability.getStartTime())
                .endTime(availability.getEndTime())
                .build();
    }

    public static void updateEntity(
            DoctorAvailability availability,
            AvailabilityRequestDTO dto) {

        availability.setDayOfWeek(dto.getDayOfWeek());
        availability.setStartTime(dto.getStartTime());
        availability.setEndTime(dto.getEndTime());
    }
}