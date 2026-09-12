package com.airtribe.meditrack.util;

import com.airtribe.meditrack.exception.InvalidDataException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.regex.Pattern;

public final class Validator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private Validator() {
    }

    public static String requireNonBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidDataException(fieldName + " must not be blank.");
        }
        return value.trim();
    }

    public static int requireValidAge(int age) {
        if (age < 0 || age > 130) {
            throw new InvalidDataException("Age must be between 0 and 130.");
        }
        return age;
    }

    public static String requireValidEmail(String email) {
        String normalizedEmail = requireNonBlank(email, "Email");
        if (!EMAIL_PATTERN.matcher(normalizedEmail).matches()) {
            throw new InvalidDataException("Email must have a valid format.");
        }
        return normalizedEmail;
    }

    public static BigDecimal requirePositiveAmount(BigDecimal amount, String fieldName) {
        Objects.requireNonNull(amount, fieldName + " must not be null.");
        if (amount.signum() < 0) {
            throw new InvalidDataException(fieldName + " must not be negative.");
        }
        return amount;
    }

    public static void requireValidAppointmentPeriod(LocalDate date, LocalTime startTime, LocalTime endTime) {
        Objects.requireNonNull(date, "Appointment date must not be null.");
        Objects.requireNonNull(startTime, "Start time must not be null.");
        Objects.requireNonNull(endTime, "End time must not be null.");
        if (!startTime.isBefore(endTime)) {
            throw new InvalidDataException("Appointment start time must be before end time.");
        }
    }
}
