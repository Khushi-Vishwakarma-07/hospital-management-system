package com.hospital.management.hospitalmanagementsystem.doctor.service;

import com.hospital.management.hospitalmanagementsystem.doctor.dto.AvailabilityRequestDTO;
import com.hospital.management.hospitalmanagementsystem.doctor.dto.AvailabilityResponseDTO;

import java.util.List;

public interface DoctorAvailabilityService {

    AvailabilityResponseDTO createAvailability(Long doctorId, AvailabilityRequestDTO request);

    AvailabilityResponseDTO getAvailabilityById(Long id);

    List<AvailabilityResponseDTO> getDoctorAvailability(Long doctorId);

    AvailabilityResponseDTO updateAvailability(Long id, AvailabilityRequestDTO request);

    void deleteAvailability(Long id);
}