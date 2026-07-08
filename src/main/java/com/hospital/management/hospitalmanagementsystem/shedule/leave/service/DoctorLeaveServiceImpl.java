package com.hospital.management.hospitalmanagementsystem.shedule.leave.service;

import com.hospital.management.hospitalmanagementsystem.common.exception.BusinessException;
import com.hospital.management.hospitalmanagementsystem.common.exception.ResourceNotFoundException;
import com.hospital.management.hospitalmanagementsystem.doctor.entity.Doctor;
import com.hospital.management.hospitalmanagementsystem.doctor.repository.DoctorRepository;
import com.hospital.management.hospitalmanagementsystem.shedule.leave.dto.*;
import com.hospital.management.hospitalmanagementsystem.shedule.leave.entity.DoctorLeave;
import com.hospital.management.hospitalmanagementsystem.shedule.leave.enums.LeaveStatus;
import com.hospital.management.hospitalmanagementsystem.shedule.leave.mapper.DoctorLeaveMapper;
import com.hospital.management.hospitalmanagementsystem.shedule.leave.repository.DoctorLeaveRepository;
import com.hospital.management.hospitalmanagementsystem.user.entity.User;
import com.hospital.management.hospitalmanagementsystem.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DoctorLeaveServiceImpl implements DoctorLeaveService {

    private final DoctorLeaveRepository repository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;


    @Transactional
    @Override
    public DoctorLeaveResponseDTO createLeave(DoctorLeaveRequestDTO dto) {

        Doctor doctor = doctorRepository.findByIdForUpdate(dto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        validateNoOverlappingLeave(
                doctor.getId(),
                dto.getStartDate(),
                dto.getEndDate(),
                null
        );

        DoctorLeave leave = DoctorLeaveMapper.toEntity(dto, doctor);

        return DoctorLeaveMapper.toDTO(repository.save(leave));
    }

    @Transactional(readOnly = true)
    @Override
    public DoctorLeaveResponseDTO getLeaveById(Long leaveId) {
        return DoctorLeaveMapper.toDTO(getLeaveOrThrow(leaveId));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<DoctorLeaveResponseDTO> getAllLeaves(Pageable pageable) {
        return repository.findAll(pageable).map(DoctorLeaveMapper::toDTO);
    }

    @Transactional
    @Override
    public DoctorLeaveResponseDTO updateLeave(Long leaveId, DoctorLeaveUpdateDTO dto) {

        DoctorLeave leave = getLeaveForUpdateOrThrow(leaveId);

        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new BusinessException("Only pending leave requests can be edited");
        }

        LocalDate newStart = dto.getStartDate() != null
                ? dto.getStartDate()
                : leave.getStartDate();

        LocalDate newEnd = dto.getEndDate() != null
                ? dto.getEndDate()
                : leave.getEndDate();

        if (newEnd.isBefore(newStart)) {
            throw new BusinessException("End date cannot be before start date");
        }

        long totalDays = ChronoUnit.DAYS.between(newStart, newEnd) + 1;

        if (totalDays > 90) {
            throw new BusinessException("Leave duration cannot exceed 90 days");
        }

        doctorRepository.findByIdForUpdate(leave.getDoctor().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        validateNoOverlappingLeave(
                leave.getDoctor().getId(),
                newStart,
                newEnd,
                leave.getId()
        );

        DoctorLeaveMapper.applyPatch(leave, dto);

        return DoctorLeaveMapper.toDTO(repository.save(leave));
    }

    @Transactional
    @Override
    public DoctorLeaveResponseDTO updateLeaveStatus(
            Long leaveId,
            DoctorLeaveStatusUpdateDTO dto) {

        DoctorLeave leave = getLeaveForUpdateOrThrow(leaveId);

        validateStatusTransition(
                leave.getStatus(),
                dto.getStatus()
        );

        leave.setStatus(dto.getStatus());

        if (dto.getStatus() == LeaveStatus.CANCELLED) {

            leave.setCancelledAt(LocalDateTime.now());
            leave.setCancellationReason(dto.getReviewComment());

        } else {

            User reviewer = userRepository.findById(dto.getReviewerId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Reviewer not found"));

            leave.setReviewer(reviewer);
            leave.setReviewedAt(LocalDateTime.now());
            leave.setReviewComment(dto.getReviewComment());
        }

        DoctorLeave saved = repository.save(leave);

        return DoctorLeaveMapper.toDTO(saved);
    }

    @Transactional
    @Override
    public void deleteLeave(Long leaveId) {

        DoctorLeave leave = getLeaveForUpdateOrThrow(leaveId);

        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new BusinessException("Only pending leaves can be deleted");
        }

        repository.delete(leave);
    }

    // ---------------- HELPERS ----------------


    private DoctorLeave getLeaveOrThrow(Long leaveId) {
        return repository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor leave not found"));
    }

    private DoctorLeave getLeaveForUpdateOrThrow(Long leaveId) {
        return repository.findByIdForUpdate(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor leave not found"));
    }

    private void validateNoOverlappingLeave(Long doctorId, LocalDate startDate, LocalDate endDate, Long excludeLeaveId) {

        if (repository.existsOverlappingLeave(
                doctorId,
                List.of(LeaveStatus.PENDING, LeaveStatus.APPROVED),
                startDate,
                endDate,
                excludeLeaveId)
        ) {

            throw new BusinessException(
                    "Doctor already has a pending or approved leave during this period");
        }
    }

    private void validateStatusTransition(LeaveStatus current, LeaveStatus target) {

        if (current == target) {
            return;
        }

        switch (current) {

            case PENDING -> {
                if (target != LeaveStatus.APPROVED
                        && target != LeaveStatus.REJECTED
                        && target != LeaveStatus.CANCELLED) {

                    throw new BusinessException(
                            "Pending leave can only be approved, rejected, or cancelled");
                }
            }

            case APPROVED -> throw new BusinessException("Approved leave cannot be changed");

            case REJECTED -> throw new BusinessException("Rejected leave cannot be changed");

            case CANCELLED -> throw new BusinessException("Cancelled leave cannot be changed");
        }
    }
}