package com.hospital.management.hospitalmanagementsystem.common.util;

import org.hibernate.exception.ConstraintViolationException;

public final class ConstraintUtils {

    private ConstraintUtils() {
    }

    public static String getConstraintName(Throwable throwable) {

        while (throwable != null) {

            if (throwable instanceof ConstraintViolationException cve) {
                return cve.getConstraintName();
            }

            throwable = throwable.getCause();
        }

        return null;
    }
}