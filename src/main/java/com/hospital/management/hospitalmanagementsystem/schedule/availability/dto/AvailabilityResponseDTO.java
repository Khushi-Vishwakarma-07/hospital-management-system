package com.hospital.management.hospitalmanagementsystem.schedule.availability.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Builder
public class AvailabilityResponseDTO {

    private Long id;

    private Long doctorId;

    private DayOfWeek dayOfWeek;

    private LocalTime startTime;

    private LocalTime endTime;
}