package com.hospital.management.hospitalmanagementsystem.doctor.service;

import com.hospital.management.hospitalmanagementsystem.appointment.repository.AppointmentRepository;
import com.hospital.management.hospitalmanagementsystem.common.exception.BusinessException;
import com.hospital.management.hospitalmanagementsystem.common.exception.DuplicateResourceException;
import com.hospital.management.hospitalmanagementsystem.common.exception.ResourceNotFoundException;
import com.hospital.management.hospitalmanagementsystem.common.util.ConstraintUtils;
import com.hospital.management.hospitalmanagementsystem.doctor.dto.DoctorRequestDTO;
import com.hospital.management.hospitalmanagementsystem.doctor.dto.DoctorResponseDTO;
import com.hospital.management.hospitalmanagementsystem.doctor.entity.Doctor;
import com.hospital.management.hospitalmanagementsystem.doctor.mapper.DoctorMapper;
import com.hospital.management.hospitalmanagementsystem.doctor.repository.DoctorRepository;
import com.hospital.management.hospitalmanagementsystem.schedule.leave.repository.DoctorLeaveRepository;
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
    private final DoctorLeaveRepository doctorLeaveRepository;

    @Override
    public DoctorResponseDTO createDoctor(DoctorRequestDTO dto) {

        validateUniqueFields(dto.getEmail(), dto.getPhoneNumber());
        Specialization specialization =
                getSpecializationForUpdateOrThrow(dto.getSpecializationId());

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

        Doctor doctor = doctorRepository.findByIdForUpdate(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with id: " + id
                        ));

        validateUniqueFieldsForUpdate(doctor, dto);

        Specialization specialization =
                getSpecializationForUpdateOrThrow(dto.getSpecializationId());

        DoctorMapper.updateEntity(doctor, dto, specialization);

        return DoctorMapper.toDTO(save(doctor));
    }

    @Override
    public void deleteDoctor(Long id) {

        Doctor doctor = doctorRepository.findByIdForUpdate(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with id: " + id
                        ));

        if (appointmentRepository.existsByDoctor_Id(id)) {
            throw new BusinessException(
                    "Cannot delete doctor with existing appointments"
            );
        }

        if (doctorLeaveRepository.existsByDoctor_Id(id)) {
            throw new BusinessException(
                    "Cannot delete doctor with existing leave records"
            );
        }

        doctorRepository.delete(doctor);
    }

    // ================= PRIVATE METHODS =================

    private Doctor getDoctorOrThrow(Long id) {

        return doctorRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with id: " + id
                        ));
    }

    private Specialization getSpecializationForUpdateOrThrow(Long id) {

        return specializationRepository.findByIdForUpdate(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Specialization not found with id: " + id
                        ));
    }

    private Doctor save(Doctor doctor) {

        try {
            return doctorRepository.saveAndFlush(doctor);

        } catch (DataIntegrityViolationException ex) {

            String constraintName = ConstraintUtils.getConstraintName(ex);

            if ("uk_doctor_phone".equals(constraintName)) {
                throw new DuplicateResourceException(
                        "A doctor with that phone number already exists"
                );
            }

            if ("uk_doctor_email".equals(constraintName)) {
                throw new DuplicateResourceException(
                        "A doctor with that email already exists"
                );
            }

            throw ex;
        }
    }

    private void validateUniqueFields(String email, String phone) {
        if (doctorRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Email already exists: " + email);
        }

        if (doctorRepository.existsByPhoneNumber(phone)) {
            throw new DuplicateResourceException("Phone already exists: " + phone);
        }
    }

    private void validateUniqueFieldsForUpdate(Doctor existing, DoctorRequestDTO dto) {

        if (!existing.getEmail().equals(dto.getEmail())
                && doctorRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + dto.getEmail());
        }

        if (!existing.getPhoneNumber().equals(dto.getPhoneNumber())
                && doctorRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new DuplicateResourceException("Phone already exists: " + dto.getPhoneNumber());
        }
    }
}