package com.booking.ticketing.utils;

import com.booking.ticketing.exceptions.DateFormatException;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private static final DateTimeFormatter YYYY_MM_DD_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter DD_MM_YYYY_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static LocalDate getLocalDate(String date) {
        switch (getDateFormat(date)) {
            case "yyyy-MM-dd":
                return LocalDate.parse(date, YYYY_MM_DD_FORMATTER);
            case "dd-MM-yyyy":
                return LocalDate.parse(date, DD_MM_YYYY_FORMATTER);
            default:
                throw new DateFormatException("Supported formats are: [\"yyyy-MM-dd\", \"dd-MM-yyyy\"]");
        }
    }

    public static LocalDateTime getLocalDateTime(String date) {
        switch (getDateFormat(date)) {
            case "yyyy-MM-dd":
                return LocalDate.parse(date, YYYY_MM_DD_FORMATTER).atStartOfDay();
            case "dd-MM-yyyy":
                return LocalDate.parse(date, DD_MM_YYYY_FORMATTER).atStartOfDay();
            default:
                throw new DateFormatException("Supported formats are: [\"yyyy-MM-dd\", \"dd-MM-yyyy\"]");
        }
    }

    public static Instant getInstant(String date) {
        switch (getDateFormat(date)) {
            case "yyyy-MM-dd":
                return LocalDate.parse(date, YYYY_MM_DD_FORMATTER).atStartOfDay().toInstant(ZoneOffset.UTC);
            case "dd-MM-yyyy":
                return LocalDate.parse(date, DD_MM_YYYY_FORMATTER).atStartOfDay().toInstant(ZoneOffset.UTC);
            default:
                throw new DateFormatException("Supported formats are: [\"yyyy-MM-dd\", \"dd-MM-yyyy\"]");
        }
    }

    private static String getDateFormat(String date) {
        if (date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return "yyyy-MM-dd";
        } else if (date.matches("\\d{2}-\\d{2}-\\d{4}")) {
            return "dd-MM-yyyy";
        } else {
            throw new DateFormatException("Invalid date format. Supported formats are: [\"yyyy-MM-dd\", \"dd-MM-yyyy\"]");
        }
    }

}