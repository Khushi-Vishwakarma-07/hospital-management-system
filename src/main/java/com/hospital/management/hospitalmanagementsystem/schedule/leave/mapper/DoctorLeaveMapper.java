package com.hospital.management.hospitalmanagementsystem.schedule.leave.mapper;

import com.hospital.management.hospitalmanagementsystem.doctor.entity.Doctor;
import com.hospital.management.hospitalmanagementsystem.schedule.leave.dto.DoctorLeaveRequestDTO;
import com.hospital.management.hospitalmanagementsystem.schedule.leave.dto.DoctorLeaveResponseDTO;
import com.hospital.management.hospitalmanagementsystem.schedule.leave.dto.DoctorLeaveUpdateDTO;
import com.hospital.management.hospitalmanagementsystem.schedule.leave.entity.DoctorLeave;
import com.hospital.management.hospitalmanagementsystem.schedule.leave.enums.LeaveStatus;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public final class DoctorLeaveMapper {

    private DoctorLeaveMapper() {
    }

    public static DoctorLeave toEntity(DoctorLeaveRequestDTO dto, Doctor doctor) {
        return DoctorLeave.builder()
                .doctor(doctor)
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .totalDays(calculateTotalDays(dto.getStartDate(), dto.getEndDate()))
                .leaveType(dto.getLeaveType())
                .reason(dto.getReason())
                .status(LeaveStatus.PENDING)
                .build();
    }

    public static DoctorLeaveResponseDTO toDTO(DoctorLeave leave) {
        return DoctorLeaveResponseDTO.builder()
                .id(leave.getId())
                .doctorId(leave.getDoctor().getId())
                .doctorName(leave.getDoctor().getFullName())
                .startDate(leave.getStartDate())
                .endDate(leave.getEndDate())
                .totalDays(leave.getTotalDays())
                .leaveType(leave.getLeaveType())
                .reason(leave.getReason())
                .status(leave.getStatus())
                .reviewComment(leave.getReviewComment())
                .reviewerId(leave.getReviewer() != null ? leave.getReviewer().getId() : null)
                .reviewerName(leave.getReviewer() != null ? leave.getReviewer().getFullName() : null)
                .reviewedAt(leave.getReviewedAt())
                .cancelledAt(leave.getCancelledAt())
                .cancellationReason(leave.getCancellationReason())
                .createdAt(leave.getCreatedAt())
                .updatedAt(leave.getUpdatedAt())
                .build();
    }

    public static void applyPatch(DoctorLeave leave, DoctorLeaveUpdateDTO dto) {
        if (dto.getStartDate() != null) {
            leave.setStartDate(dto.getStartDate());
        }
        if (dto.getEndDate() != null) {
            leave.setEndDate(dto.getEndDate());
        }
        if (dto.getLeaveType() != null) {
            leave.setLeaveType(dto.getLeaveType());
        }
        if (dto.getReason() != null) {
            leave.setReason(dto.getReason());
        }
        leave.setTotalDays(calculateTotalDays(leave.getStartDate(), leave.getEndDate()));
    }

    private static int calculateTotalDays(LocalDate start, LocalDate end) {
        return (int) (ChronoUnit.DAYS.between(start, end) + 1);
    }
}