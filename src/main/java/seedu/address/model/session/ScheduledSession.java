package seedu.address.model.session;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import seedu.address.commons.util.DateTimeUtil;
import seedu.address.model.attendance.AttendanceHistory;
import seedu.address.model.recurrence.Recurrence;

/**
 * Immutable scheduled session value object.
 */
public final class ScheduledSession {

    private final Recurrence recurrence;
    private final LocalDateTime start;
    private final LocalDateTime next;
    private final AttendanceHistory attendanceHistory;
    private final String description;

    /**
     * Creates a {@code ScheduledSession}.
     */
    public ScheduledSession(Recurrence recurrence, LocalDateTime start, LocalDateTime next,
                            AttendanceHistory attendanceHistory, String description) {
        requireAllNonNull(recurrence, start, next, attendanceHistory, description);
        this.recurrence = recurrence;
        this.start = DateTimeUtil.normalizeToMinute(start);
        this.next = DateTimeUtil.normalizeToMinute(next);
        this.attendanceHistory = attendanceHistory;
        this.description = description.trim();
    }

    public Recurrence getRecurrence() {
        return recurrence;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getNext() {
        return next;
    }

    public AttendanceHistory getAttendanceHistory() {
        return attendanceHistory;
    }



    public String getDescription() {
        return description;
    }

    /**
     * Returns a copy with updated attendance history.
     */
    public ScheduledSession withAttendance(AttendanceHistory updatedAttendanceHistory) {
        requireAllNonNull(updatedAttendanceHistory);
        return new ScheduledSession(recurrence, start, next, updatedAttendanceHistory, description);
    }

    /**
     * Returns a copy with next session advanced by one recurrence cycle.
     */
    public ScheduledSession withAdvancedNext() {
        if (recurrence == Recurrence.NONE) {
            return this;
        }
        LocalDate advancedDate = recurrence.next(next.toLocalDate());
        LocalDateTime advancedDateTime = LocalDateTime.of(advancedDate, next.toLocalTime());
        return new ScheduledSession(recurrence, start, advancedDateTime, attendanceHistory, description);
    }

    /**
     * Returns a copy with next session rolled back by one recurrence cycle.
     */
    public ScheduledSession withRolledBackNext() {
        if (recurrence == Recurrence.NONE) {
            return this;
        }
        LocalDate previousDate = recurrence.previous(next.toLocalDate());
        LocalDateTime previousDateTime = LocalDateTime.of(previousDate, next.toLocalTime());
        return new ScheduledSession(recurrence, start, previousDateTime, attendanceHistory, description);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ScheduledSession)) {
            return false;
        }

        ScheduledSession otherSession = (ScheduledSession) other;
        return recurrence.equals(otherSession.recurrence)
                && start.equals(otherSession.start)
                && next.equals(otherSession.next)
                && attendanceHistory.equals(otherSession.attendanceHistory)
                && description.equals(otherSession.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recurrence, start, next, attendanceHistory, description);
    }

    @Override
    public String toString() {
        return "ScheduledSession{"
                + "recurrence=" + recurrence
                + ", start=" + start
                + ", next=" + next
                + ", attendanceHistory=" + attendanceHistory
                + ", description='" + description + '\''
                + "}";
    }
}
