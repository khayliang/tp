package seedu.address.model.session;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDateTime;
import java.util.Objects;

import seedu.address.commons.util.DateTimeUtil;
import seedu.address.model.attendance.AttendanceRecords;
import seedu.address.model.recurrence.Recurrence;

/**
 * Immutable appointment value object.
 */
public final class Appointment {

    private final Recurrence recurrence;
    private final LocalDateTime start;
    private final LocalDateTime next;
    private final AttendanceRecords attendance;
    private final String description;

    /**
     * Creates an {@code Appointment}.
     */
    public Appointment(Recurrence recurrence, LocalDateTime start, LocalDateTime next,
                       AttendanceRecords attendance, String description) {
        requireAllNonNull(recurrence, start, next, attendance, description);
        this.recurrence = recurrence;
        this.start = DateTimeUtil.normalizeToMinute(start);
        this.next = DateTimeUtil.normalizeToMinute(next);
        this.attendance = attendance;
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

    public AttendanceRecords getAttendance() {
        return attendance;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Returns a copy with updated attendance.
     */
    public Appointment withAttendance(AttendanceRecords updatedAttendance) {
        requireAllNonNull(updatedAttendance);
        return new Appointment(recurrence, start, next, updatedAttendance, description);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Appointment)) {
            return false;
        }

        Appointment otherAppointment = (Appointment) other;
        return recurrence.equals(otherAppointment.recurrence)
                && start.equals(otherAppointment.start)
                && next.equals(otherAppointment.next)
                && attendance.equals(otherAppointment.attendance)
                && description.equals(otherAppointment.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recurrence, start, next, attendance, description);
    }

    @Override
    public String toString() {
        return "Appointment{"
                + "recurrence=" + recurrence
                + ", start=" + start
                + ", next=" + next
                + ", attendance=" + attendance
                + ", description='" + description + '\''
                + "}";
    }
}
