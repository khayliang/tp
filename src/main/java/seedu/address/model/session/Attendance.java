package seedu.address.model.session;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.util.DateTimeUtil;

/**
 * Immutable attendance history value object.
 */
public class Attendance {

    public static final Attendance EMPTY = new Attendance();

    private final LinkedHashSet<LocalDateTime> history;

    /**
     * Creates an empty attendance history.
     */
    public Attendance() {
        this.history = new LinkedHashSet<>();
    }

    /**
     * Creates an attendance history from the provided date-times.
     */
    public Attendance(LocalDateTime... attendanceDates) {
        requireAllNonNull((Object[]) attendanceDates);
        this.history = new LinkedHashSet<>();
        for (LocalDateTime attendanceDate : attendanceDates) {
            this.history.add(DateTimeUtil.normalizeToMinute(attendanceDate));
        }
    }

    private Attendance(LinkedHashSet<LocalDateTime> history) {
        this.history = history;
    }

    /**
     * Returns a new {@code Attendance} with {@code attendanceDate} added.
     * Exact duplicate timestamps are deduplicated.
     */
    public Attendance addAttendance(LocalDateTime attendanceDate) {
        requireAllNonNull(attendanceDate);
        LinkedHashSet<LocalDateTime> updatedHistory = new LinkedHashSet<>(history);
        updatedHistory.add(DateTimeUtil.normalizeToMinute(attendanceDate));
        return new Attendance(updatedHistory);
    }

    /**
     * Returns the most recently added attendance entry, if any.
     */
    public Optional<LocalDateTime> getLastAttendance() {
        if (history.isEmpty()) {
            return Optional.empty();
        }

        LocalDateTime latest = null;
        for (LocalDateTime attendanceDate : history) {
            latest = attendanceDate;
        }
        return Optional.ofNullable(latest);
    }

    /**
     * Returns attendance history in insertion order.
     */
    public Set<LocalDateTime> getHistory() {
        return Collections.unmodifiableSet(history);
    }

    /**
     * Returns attendance history in reverse chronological order.
     */
    public List<LocalDateTime> getHistoryDescending() {
        List<LocalDateTime> ordered = new ArrayList<>(history);
        ordered.sort(Collections.reverseOrder());
        return ordered;
    }

    /**
     * Returns attendance entries on or after {@code fromDateTime} in reverse chronological order.
     * This supports future appointment schedule matching without coupling to appointment models.
     */
    public List<LocalDateTime> getHistoryOnOrAfter(LocalDateTime fromDateTime) {
        requireAllNonNull(fromDateTime);
        LocalDateTime normalizedFromDateTime = DateTimeUtil.normalizeToMinute(fromDateTime);
        List<LocalDateTime> filtered = new ArrayList<>();
        for (LocalDateTime attendanceDate : history) {
            if (!attendanceDate.isBefore(normalizedFromDateTime)) {
                filtered.add(attendanceDate);
            }
        }
        filtered.sort(Collections.reverseOrder());
        return filtered;
    }

    public boolean isEmpty() {
        return history.isEmpty();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Attendance)) {
            return false;
        }

        Attendance otherAttendance = (Attendance) other;
        return history.equals(otherAttendance.history);
    }

    @Override
    public int hashCode() {
        return Objects.hash(history);
    }

    @Override
    public String toString() {
        return history.toString();
    }
}
