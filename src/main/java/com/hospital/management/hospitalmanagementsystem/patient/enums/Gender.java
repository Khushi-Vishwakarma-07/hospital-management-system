package com.hospital.management.hospitalmanagementsystem.patient.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.hospital.management.hospitalmanagementsystem.common.exception.InvalidEnumValueException;

public enum Gender {

    MALE("male"),
    FEMALE("female"),
    OTHER("other");

    private final String label;

    Gender(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static Gender fromValue(String value) {
        if (value == null) throw new InvalidEnumValueException("gender", "null");

        for (Gender g : values()) {
            if (g.label.equalsIgnoreCase(value) || g.name().equalsIgnoreCase(value)) return g;
        }
        throw new InvalidEnumValueException("gender", value);
    }
}