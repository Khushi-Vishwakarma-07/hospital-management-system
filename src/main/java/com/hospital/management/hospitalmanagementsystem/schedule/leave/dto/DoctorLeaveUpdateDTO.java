package com.hospital.management.hospitalmanagementsystem.schedule.leave.dto;

import com.hospital.management.hospitalmanagementsystem.schedule.leave.enums.LeaveType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorLeaveUpdateDTO {

    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;

    @FutureOrPresent(message = "End date must be today or in the future")
    private LocalDate endDate;

    private LeaveType leaveType;

    @Size(min = 5, max = 500, message = "Reason must be between 5 and 500 characters")
    private String reason;
}