package com.hospital.management.hospitalmanagementsystem.appointment.enums;

public enum AppointmentStatus {

    SCHEDULED,
    CONFIRMED,
    COMPLETED,
    CANCELLED,
    NO_SHOW;

    public boolean canTransitionTo(AppointmentStatus target) {
        return switch (this) {
            case SCHEDULED ->
                    target == SCHEDULED
                            || target == CONFIRMED
                            || target == CANCELLED;

            case CONFIRMED ->
                    target == CONFIRMED
                            || target == COMPLETED
                            || target == CANCELLED
                            || target == NO_SHOW;

            case COMPLETED,
                 CANCELLED,
                 NO_SHOW -> false;
        };
    }
}