package com.hospital.management.hospitalmanagementsystem.shedule.availability.service;

import com.hospital.management.hospitalmanagementsystem.shedule.availability.dto.AvailabilityRequestDTO;
import com.hospital.management.hospitalmanagementsystem.shedule.availability.dto.AvailabilityResponseDTO;

import java.time.DayOfWeek;
import java.util.List;

public interface DoctorAvailabilityService {

    AvailabilityResponseDTO createAvailability(Long doctorId, AvailabilityRequestDTO request);

    AvailabilityResponseDTO getAvailabilityById(Long id);

    List<AvailabilityResponseDTO> getDoctorAvailabilities(Long doctorId, DayOfWeek dayOfWeek);

    AvailabilityResponseDTO updateAvailability(Long id, AvailabilityRequestDTO request);

    List<AvailabilityResponseDTO> replaceWeeklySchedule(Long doctorId, List<AvailabilityRequestDTO> requests);

    void deleteAvailability(Long id);
}