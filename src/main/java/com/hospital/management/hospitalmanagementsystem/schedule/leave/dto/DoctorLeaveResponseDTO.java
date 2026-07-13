package com.hospital.management.hospitalmanagementsystem.schedule.leave.dto;

import com.hospital.management.hospitalmanagementsystem.schedule.leave.enums.LeaveStatus;
import com.hospital.management.hospitalmanagementsystem.schedule.leave.enums.LeaveType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorLeaveResponseDTO {

    private Long id;

    private Long doctorId;

    private String doctorName;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer totalDays;

    private LeaveType leaveType;

    private String reason;

    private LeaveStatus status;

    private String reviewComment;

    private Long reviewerId;

    private String reviewerName;

    private LocalDateTime reviewedAt;

    private LocalDateTime cancelledAt;

    private String cancellationReason;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}