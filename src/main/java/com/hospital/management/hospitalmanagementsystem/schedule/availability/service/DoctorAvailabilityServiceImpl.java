package com.hospital.management.hospitalmanagementsystem.schedule.availability.service;

import com.hospital.management.hospitalmanagementsystem.common.exception.BusinessException;
import com.hospital.management.hospitalmanagementsystem.common.exception.ResourceNotFoundException;
import com.hospital.management.hospitalmanagementsystem.schedule.availability.dto.AvailabilityRequestDTO;
import com.hospital.management.hospitalmanagementsystem.schedule.availability.dto.AvailabilityResponseDTO;
import com.hospital.management.hospitalmanagementsystem.doctor.entity.Doctor;
import com.hospital.management.hospitalmanagementsystem.schedule.availability.entity.DoctorAvailability;
import com.hospital.management.hospitalmanagementsystem.schedule.availability.mapper.DoctorAvailabilityMapper;
import com.hospital.management.hospitalmanagementsystem.schedule.availability.repository.DoctorAvailabilityRepository;
import com.hospital.management.hospitalmanagementsystem.doctor.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.Duration;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class DoctorAvailabilityServiceImpl implements DoctorAvailabilityService {

    private static final long MIN_SLOT_MINUTES = 15;
    private static final long MAX_SLOT_MINUTES = 12 * 60;

    private final DoctorAvailabilityRepository repository;
    private final DoctorRepository doctorRepository;

    @Override
    @Transactional
    public AvailabilityResponseDTO createAvailability(Long doctorId,
                                                      AvailabilityRequestDTO dto) {

        Doctor doctor = getDoctorForScheduleUpdate(doctorId);

        validateAvailabilityForCreate(doctorId, dto);

        DoctorAvailability availability = DoctorAvailabilityMapper.toEntity(dto);
        availability.setDoctor(doctor);

        DoctorAvailability saved = repository.save(availability);

        return DoctorAvailabilityMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AvailabilityResponseDTO getAvailabilityById(Long id) {

        return DoctorAvailabilityMapper.toDTO(getAvailabilityOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvailabilityResponseDTO> getDoctorAvailabilities(Long doctorId, DayOfWeek dayOfWeek) {

        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found with id: " + doctorId);
        }

        List<DoctorAvailability> results = (dayOfWeek != null)
                ? repository.findByDoctor_IdAndDayOfWeekOrderByStartTimeAsc(doctorId, dayOfWeek)
                : repository.findByDoctor_IdOrderByDayOfWeekAscStartTimeAsc(doctorId);

        return results.stream()
                .map(DoctorAvailabilityMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public AvailabilityResponseDTO updateAvailability(Long id, AvailabilityRequestDTO dto) {

        DoctorAvailability unlocked = getAvailabilityOrThrow(id);

        getDoctorForScheduleUpdate(unlocked.getDoctor().getId());

        DoctorAvailability availability = getAvailabilityForUpdateOrThrow(id);

        validateAvailabilityForUpdate(availability, dto);
        DoctorAvailabilityMapper.updateEntity(availability, dto);

        return DoctorAvailabilityMapper.toDTO(repository.save(availability));
    }

    @Override
    @Transactional
    public void deleteAvailability(Long id) {

        DoctorAvailability unlocked = getAvailabilityOrThrow(id);

        getDoctorForScheduleUpdate(unlocked.getDoctor().getId());

        DoctorAvailability availability = getAvailabilityForUpdateOrThrow(id);

        repository.delete(availability);
    }

    @Override
    public List<AvailabilityResponseDTO> replaceWeeklySchedule(Long doctorId,
                                                               List<AvailabilityRequestDTO> requests) {

        Doctor doctor = getDoctorForScheduleUpdate(doctorId);

        requests.forEach(this::validateTime);
        requests.forEach(this::validateBusinessRules);
        validateNoInternalOverlaps(requests);

        repository.deleteByDoctor_Id(doctorId);
        repository.flush();

        List<DoctorAvailability> entities = requests.stream()
                .map(dto -> {
                    DoctorAvailability entity = DoctorAvailabilityMapper.toEntity(dto);
                    entity.setDoctor(doctor);
                    return entity;
                })
                .toList();

        List<DoctorAvailability> saved = repository.saveAll(entities);

        return saved.stream()
                .map(DoctorAvailabilityMapper::toDTO)
                .toList();
    }

    // ------------------- VALIDATION -------------------

    private void validateAvailabilityForCreate(Long doctorId,
                                               AvailabilityRequestDTO dto) {

        validateTime(dto);
        validateBusinessRules(dto);
        checkOverlap(dto, doctorId, null);
    }

    private void validateAvailabilityForUpdate(DoctorAvailability availability,
                                               AvailabilityRequestDTO dto) {

        validateTime(dto);
        validateBusinessRules(dto);
        checkOverlap(
                dto,
                availability.getDoctor().getId(),
                availability.getId()
        );
    }

    private void validateTime(AvailabilityRequestDTO dto) {

        if (dto.getStartTime().equals(dto.getEndTime())) {
            throw new BusinessException("Start time and end time cannot be same");
        }

        if (!dto.getStartTime().isBefore(dto.getEndTime())) {
            throw new BusinessException("Start time must be before end time");
        }
    }

    private void validateBusinessRules(AvailabilityRequestDTO dto) {

        long minutes = Duration.between(dto.getStartTime(), dto.getEndTime()).toMinutes();

        if (minutes < MIN_SLOT_MINUTES) {
            throw new BusinessException("Minimum slot duration is 15 minutes");
        }

        if (minutes > MAX_SLOT_MINUTES) {
            throw new BusinessException("Maximum shift duration is 12 hours");
        }
    }

    private void checkOverlap(AvailabilityRequestDTO dto, Long doctorId, Long excludeId) {

        boolean hasOverlap = repository.existsOverlappingSlot(
                doctorId,
                dto.getDayOfWeek(),
                dto.getStartTime(),
                dto.getEndTime(),
                excludeId
        );

        if (hasOverlap) {
            throw new BusinessException(
                    "Doctor already has overlapping availability on " + dto.getDayOfWeek());
        }
    }

    private void validateNoInternalOverlaps(List<AvailabilityRequestDTO> requests) {

        for (int i = 0; i < requests.size(); i++) {
            AvailabilityRequestDTO a = requests.get(i);

            for (int j = i + 1; j < requests.size(); j++) {
                AvailabilityRequestDTO b = requests.get(j);

                boolean sameDay = a.getDayOfWeek() == b.getDayOfWeek();
                boolean overlaps = a.getStartTime().isBefore(b.getEndTime())
                        && b.getStartTime().isBefore(a.getEndTime());

                if (sameDay && overlaps) {
                    throw new BusinessException(
                            "Submitted schedule has overlapping slots on " + a.getDayOfWeek());
                }
            }
        }
    }

    private Doctor getDoctorForScheduleUpdate(Long doctorId) {

        return doctorRepository.findByIdForUpdate(doctorId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with id: " + doctorId));
    }

    private DoctorAvailability getAvailabilityOrThrow(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Availability not found with id: " + id));
    }

    private DoctorAvailability getAvailabilityForUpdateOrThrow(Long id) {

        return repository.findByIdForUpdate(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Availability not found with id: " + id));
    }
}