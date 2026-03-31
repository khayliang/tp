package seedu.address.commons.util;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Application-wide clock utilities using Singapore time.
 */
public final class AppClock {

    private static final ZoneId SINGAPORE_ZONE_ID = ZoneId.of("Asia/Singapore");
    private static final Clock SINGAPORE_CLOCK = Clock.system(SINGAPORE_ZONE_ID);

    private AppClock() {}

    /**
     * Returns the app-wide Singapore clock.
     */
    public static Clock getClock() {
        return SINGAPORE_CLOCK;
    }

    /**
     * Returns today's date in Singapore.
     */
    public static LocalDate today() {
        return LocalDate.now(SINGAPORE_CLOCK);
    }

    /**
     * Returns current date-time in Singapore.
     */
    public static LocalDateTime now() {
        return LocalDateTime.now(SINGAPORE_CLOCK);
    }
}
