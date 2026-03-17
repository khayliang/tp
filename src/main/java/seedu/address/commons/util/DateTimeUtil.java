package seedu.address.commons.util;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

/**
 * Utility methods for parsing and formatting date-time values in ISO local format.
 */
public final class DateTimeUtil {

    public static final String ISO_LOCAL_DATE_TIME_EXAMPLE = "2026-01-13T08:00:00";
    private static final String INVALID_ISO_LOCAL_DATE_TIME_MESSAGE_TEMPLATE =
            "%s must be in ISO 8601 local format, e.g. %s";

    private static final DateTimeFormatter ISO_LOCAL_DATE_TIME_FORMATTER =
            DateTimeFormatter.ISO_LOCAL_DATE_TIME.withResolverStyle(ResolverStyle.STRICT);

    private DateTimeUtil() {}

    /**
     * Returns an error message describing the expected ISO local date-time format.
     */
    public static String getInvalidIsoLocalDateTimeMessage(String fieldName) {
        requireNonNull(fieldName);
        return String.format(INVALID_ISO_LOCAL_DATE_TIME_MESSAGE_TEMPLATE, fieldName, ISO_LOCAL_DATE_TIME_EXAMPLE);
    }

    /**
     * Parses an ISO local date-time string with strict resolver style.
     */
    public static LocalDateTime parseIsoLocalDateTime(String value) {
        requireNonNull(value);
        return LocalDateTime.parse(value.trim(), ISO_LOCAL_DATE_TIME_FORMATTER);
    }

    /**
     * Formats a {@code LocalDateTime} in ISO local date-time format.
     */
    public static String formatIsoLocalDateTime(LocalDateTime value) {
        requireNonNull(value);
        return value.format(ISO_LOCAL_DATE_TIME_FORMATTER);
    }
}
