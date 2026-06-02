package com.hospital.management.hospitalmanagementsystem.common.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}