package com.hospital.management.hospitalmanagementsystem.doctor.service;

import com.hospital.management.hospitalmanagementsystem.doctor.dto.DoctorRequestDTO;
import com.hospital.management.hospitalmanagementsystem.doctor.dto.DoctorResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DoctorService {

    DoctorResponseDTO createDoctor(DoctorRequestDTO dto);

    DoctorResponseDTO getDoctorById(Long id);

    Page<DoctorResponseDTO> getAllDoctors(Pageable pageable);

    DoctorResponseDTO updateDoctor(Long id, DoctorRequestDTO dto);

    void deleteDoctor(Long id);
}