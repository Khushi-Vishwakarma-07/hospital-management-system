package com.hospital.management.hospitalmanagementsystem.schedule.leave.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hospital.management.hospitalmanagementsystem.schedule.leave.enums.LeaveType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorLeaveRequestDTO {

    private static final int MAX_LEAVE_DURATION_DAYS = 90;

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @FutureOrPresent(message = "End date must be today or in the future")
    private LocalDate endDate;

    @NotNull(message = "Leave type is required")
    private LeaveType leaveType;

    @NotBlank(message = "Reason is required")
    @Size(min = 5, max = 500, message = "Reason must be between 5 and 500 characters")
    private String reason;

    @SuppressWarnings("unused")
    @JsonIgnore
    @AssertTrue(message = "End date cannot be before start date")
    public boolean isDateRangeValid() {
        if (startDate == null || endDate == null) {
            return true;
        }
        return !endDate.isBefore(startDate);
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    @AssertTrue(message = "Leave duration cannot exceed " + MAX_LEAVE_DURATION_DAYS + " days")
    public boolean isDurationWithinLimit() {
        if (startDate == null || endDate == null) {
            return true;
        }

        long totalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        return totalDays <= MAX_LEAVE_DURATION_DAYS;
    }
}