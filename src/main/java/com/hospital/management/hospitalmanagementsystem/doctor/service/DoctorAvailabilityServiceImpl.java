package com.hospital.management.hospitalmanagementsystem.doctor.service;

import com.hospital.management.hospitalmanagementsystem.common.exception.BusinessException;
import com.hospital.management.hospitalmanagementsystem.common.exception.ResourceNotFoundException;
import com.hospital.management.hospitalmanagementsystem.doctor.dto.AvailabilityRequestDTO;
import com.hospital.management.hospitalmanagementsystem.doctor.dto.AvailabilityResponseDTO;
import com.hospital.management.hospitalmanagementsystem.doctor.entity.Doctor;
import com.hospital.management.hospitalmanagementsystem.doctor.entity.DoctorAvailability;
import com.hospital.management.hospitalmanagementsystem.doctor.mapper.DoctorAvailabilityMapper;
import com.hospital.management.hospitalmanagementsystem.doctor.repository.DoctorAvailabilityRepository;
import com.hospital.management.hospitalmanagementsystem.doctor.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorAvailabilityServiceImpl implements DoctorAvailabilityService {

    private final DoctorAvailabilityRepository repository;
    private final DoctorRepository doctorRepository;

    @Override
    public AvailabilityResponseDTO createAvailability(Long doctorId, AvailabilityRequestDTO dto) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doctor not found with id: " + doctorId));

        validateTime(dto);
        validateBusinessRules(dto);
        checkOverlap(dto, doctorId, null);

        DoctorAvailability availability = DoctorAvailabilityMapper.toEntity(dto);
        availability.setDoctor(doctor);

        return DoctorAvailabilityMapper.toDTO(repository.save(availability));
    }

    @Override
    @Transactional(readOnly = true)
    public AvailabilityResponseDTO getAvailabilityById(Long id) {

        DoctorAvailability availability = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Availability not found with id: " + id));

        return DoctorAvailabilityMapper.toDTO(availability);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvailabilityResponseDTO> getDoctorAvailabilities(Long doctorId) {

        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found with id: " + doctorId);
        }

        return repository.findByDoctor_IdOrderByDayOfWeekAscStartTimeAsc(doctorId)
                .stream()
                .map(DoctorAvailabilityMapper::toDTO)
                .toList();
    }

    @Override
    public AvailabilityResponseDTO updateAvailability(Long id, AvailabilityRequestDTO dto) {

        DoctorAvailability availability = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Availability not found with id: " + id));

        validateTime(dto);
        validateBusinessRules(dto);
        checkOverlap(dto, availability.getDoctor().getId(), id);

        DoctorAvailabilityMapper.updateEntity(availability, dto);

        return DoctorAvailabilityMapper.toDTO(repository.save(availability));
    }

    @Override
    public void deleteAvailability(Long id) {

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Availability not found with id: " + id);
        }

        repository.deleteById(id);
    }

    // ------------------- VALIDATION -------------------

    private void validateTime(AvailabilityRequestDTO dto) {


        if (dto.getStartTime().equals(dto.getEndTime())) {
            throw new BusinessException("Start time and end time cannot be same");
        }

        if (!dto.getStartTime().isBefore(dto.getEndTime())) {
            throw new BusinessException("Start time must be before end time");
        }
    }

    private void validateBusinessRules(AvailabilityRequestDTO dto) {

        long minutes = java.time.Duration.between(
                dto.getStartTime(),
                dto.getEndTime()
        ).toMinutes();

        if (minutes < 15) {
            throw new BusinessException("Minimum slot duration is 15 minutes");
        }

        if (minutes > 12 * 60) {
            throw new BusinessException("Maximum shift duration is 12 hours");
        }
    }

    private void checkOverlap(AvailabilityRequestDTO dto,
                              Long doctorId,
                              Long excludeId) {

        boolean hasOverlap =
                repository.existsOverlappingSlot(
                        doctorId,
                        dto.getDayOfWeek(),
                        dto.getStartTime(),
                        dto.getEndTime(),
                        excludeId
                );

        if (hasOverlap) {
            throw new BusinessException(
                    "Doctor already has overlapping availability on " + dto.getDayOfWeek()
            );
        }
    }
}