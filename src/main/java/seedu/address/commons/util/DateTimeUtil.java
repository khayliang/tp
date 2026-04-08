package seedu.address.commons.util;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

/**
 * Utility constants and methods for working with date and date-time values.
 */
public class DateTimeUtil {

    public static final DateTimeFormatter ISO_LOCAL_DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    public static final DateTimeFormatter ISO_LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    public static final DateTimeFormatter ISO_LOCAL_DATE_STRICT_FORMATTER =
        ISO_LOCAL_DATE_FORMATTER.withResolverStyle(ResolverStyle.STRICT);
    public static final DateTimeFormatter ISO_LOCAL_DATE_TIME_STRICT_FORMATTER =
        ISO_LOCAL_DATE_TIME_FORMATTER.withResolverStyle(ResolverStyle.STRICT);

    private DateTimeUtil() {}

    /**
     * Normalizes {@code dateTime} to minute precision by dropping seconds and nanoseconds.
     */
    public static LocalDateTime normalizeToMinute(LocalDateTime dateTime) {
        requireNonNull(dateTime);
        return dateTime.withSecond(0).withNano(0);
    }
}
