package com.airtribe.meditrack.util;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.exception.InvalidDataException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public final class DateUtil {
    private DateUtil() {
    }

    public static LocalDate parseDate(String value) {
        try {
            return LocalDate.parse(value, Constants.DATE_FORMAT);
        } catch (DateTimeParseException exception) {
            throw new InvalidDataException("Date must use the format yyyy-MM-dd.", exception);
        }
    }

    public static LocalTime parseTime(String value) {
        try {
            return LocalTime.parse(value, Constants.TIME_FORMAT);
        } catch (DateTimeParseException exception) {
            throw new InvalidDataException("Time must use the format HH:mm.", exception);
        }
    }

    public static String formatDate(LocalDate date) {
        return date.format(Constants.DATE_FORMAT);
    }

    public static String formatTime(LocalTime time) {
        return time.format(Constants.TIME_FORMAT);
    }
}
