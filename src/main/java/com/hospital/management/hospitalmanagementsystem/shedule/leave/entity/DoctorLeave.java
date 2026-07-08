package com.hospital.management.hospitalmanagementsystem.shedule.leave.entity;

import com.hospital.management.hospitalmanagementsystem.common.base.BaseEntity;
import com.hospital.management.hospitalmanagementsystem.doctor.entity.Doctor;
import com.hospital.management.hospitalmanagementsystem.shedule.leave.enums.LeaveStatus;
import com.hospital.management.hospitalmanagementsystem.shedule.leave.enums.LeaveType;
import com.hospital.management.hospitalmanagementsystem.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "doctor_leaves",
        indexes = {
                @Index(
                        name = "idx_leave_doctor",
                        columnList = "doctor_id"
                ),
                @Index(
                        name = "idx_leave_status",
                        columnList = "status"
                ),
                @Index(
                        name = "idx_leave_dates",
                        columnList = "start_date,end_date"
                ),
                @Index(
                        name = "idx_leave_doctor_dates",
                        columnList = "doctor_id,start_date,end_date"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorLeave extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    private User reviewer;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "total_days", nullable = false)
    private Integer totalDays;

    @Enumerated(EnumType.STRING)
    @Column(name = "leave_type", nullable = false, length = 30)
    private LeaveType leaveType;

    @Column(nullable = false, length = 500)
    private String reason;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private LeaveStatus status = LeaveStatus.PENDING;

    @Column(name = "review_comment", length = 500)
    private String reviewComment;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;
}