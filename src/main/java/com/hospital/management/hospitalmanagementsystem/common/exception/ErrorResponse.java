package com.hospital.management.hospitalmanagementsystem.common.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class ErrorResponse {

    private final int status;

    private final String message;

    private final LocalDateTime timestamp;

    private final Map<String, String> errors;
}