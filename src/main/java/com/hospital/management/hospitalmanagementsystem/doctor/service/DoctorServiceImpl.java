package com.hospital.management.hospitalmanagementsystem.doctor.service;

import com.hospital.management.hospitalmanagementsystem.appointment.repository.AppointmentRepository;
import com.hospital.management.hospitalmanagementsystem.common.exception.BusinessException;
import com.hospital.management.hospitalmanagementsystem.common.exception.DuplicateResourceException;
import com.hospital.management.hospitalmanagementsystem.common.exception.ResourceNotFoundException;
import com.hospital.management.hospitalmanagementsystem.doctor.dto.DoctorRequestDTO;
import com.hospital.management.hospitalmanagementsystem.doctor.dto.DoctorResponseDTO;
import com.hospital.management.hospitalmanagementsystem.doctor.entity.Doctor;
import com.hospital.management.hospitalmanagementsystem.doctor.mapper.DoctorMapper;
import com.hospital.management.hospitalmanagementsystem.doctor.repository.DoctorRepository;
import com.hospital.management.hospitalmanagementsystem.specialization.entity.Specialization;
import com.hospital.management.hospitalmanagementsystem.specialization.repository.SpecializationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final SpecializationRepository specializationRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public DoctorResponseDTO createDoctor(DoctorRequestDTO dto) {

        Specialization specialization =
                getSpecializationOrThrow(dto.getSpecializationId());

        Doctor doctor = DoctorMapper.toEntity(dto, specialization);

        return DoctorMapper.toDTO(save(doctor));
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorResponseDTO getDoctorById(Long id) {

        return DoctorMapper.toDTO(getDoctorOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DoctorResponseDTO> getAllDoctors(Pageable pageable) {

        return doctorRepository.findAll(pageable)
                .map(DoctorMapper::toDTO);
    }

    @Override
    public DoctorResponseDTO updateDoctor(Long id, DoctorRequestDTO dto) {

        Doctor doctor = getDoctorOrThrow(id);

        Specialization specialization =
                getSpecializationOrThrow(dto.getSpecializationId());

        DoctorMapper.updateEntity(doctor, dto, specialization);

        return DoctorMapper.toDTO(save(doctor));
    }

    @Override
    public void deleteDoctor(Long id) {

        Doctor doctor = getDoctorOrThrow(id);

        if (appointmentRepository.existsByDoctor_Id(id)) {
            throw new BusinessException(
                    "Cannot delete doctor with existing appointments"
            );
        }

        doctorRepository.delete(doctor);
    }

    // ================= PRIVATE METHODS  =================

    private Doctor getDoctorOrThrow(Long id) {

        return doctorRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with id: " + id
                        )
                );
    }

    private Specialization getSpecializationOrThrow(Long id) {

        return specializationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Specialization not found with id: " + id
                        )
                );
    }

    private Doctor save(Doctor doctor) {

        try {
            return doctorRepository.saveAndFlush(doctor);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateResourceException(
                    "A doctor with that email or phone number already exists"
            );
        }
    }
}