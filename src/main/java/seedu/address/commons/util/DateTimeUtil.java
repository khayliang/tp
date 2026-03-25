package seedu.address.commons.util;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;

/**
 * Utility methods for working with {@link LocalDateTime} values.
 */
public class DateTimeUtil {

    private DateTimeUtil() {}

    /**
     * Normalizes {@code dateTime} to minute precision by dropping seconds and nanoseconds.
     */
    public static LocalDateTime normalizeToMinute(LocalDateTime dateTime) {
        requireNonNull(dateTime);
        return dateTime.withSecond(0).withNano(0);
    }
}
