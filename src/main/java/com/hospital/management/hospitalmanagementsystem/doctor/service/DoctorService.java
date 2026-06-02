package com.hospital.management.hospitalmanagementsystem.doctor.service;

import com.hospital.management.hospitalmanagementsystem.doctor.dto.DoctorRequestDTO;
import com.hospital.management.hospitalmanagementsystem.doctor.dto.DoctorResponseDTO;

import java.util.List;

public interface DoctorService {

    DoctorResponseDTO createDoctor(
            DoctorRequestDTO dto);

    DoctorResponseDTO getDoctorById(
            Long id);

    List<DoctorResponseDTO> getAllDoctors();

    DoctorResponseDTO updateDoctor(
            Long id,
            DoctorRequestDTO dto);

    void deleteDoctor(Long id);
}