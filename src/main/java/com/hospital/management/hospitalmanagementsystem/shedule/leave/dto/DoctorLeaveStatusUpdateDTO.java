package com.hospital.management.hospitalmanagementsystem.shedule.leave.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hospital.management.hospitalmanagementsystem.shedule.leave.enums.LeaveStatus;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorLeaveStatusUpdateDTO {

    @NotNull(message = "Status is required")
    private LeaveStatus status;

    @Size(max = 500, message = "Review comment cannot exceed 500 characters")
    private String reviewComment;

    private Long reviewerId;

    @SuppressWarnings("unused")
    @AssertTrue(message = "reviewerId is required when approving or rejecting a leave")
    @JsonIgnore
    public boolean isReviewerProvidedWhenRequired() {
        if (status == null) {
            return true; // let @NotNull report the real problem
        }
        if (status == LeaveStatus.APPROVED || status == LeaveStatus.REJECTED) {
            return reviewerId != null;
        }
        return true;
    }
}