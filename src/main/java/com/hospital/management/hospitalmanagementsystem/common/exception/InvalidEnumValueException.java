package com.hospital.management.hospitalmanagementsystem.common.exception;

public class InvalidEnumValueException extends BusinessException {

    public InvalidEnumValueException(String field, String value) {
        super("Invalid value for " + field + ": " + value);
    }
}