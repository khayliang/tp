package seedu.address.model.attendance;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Immutable attendance record for a scheduled appointment.
 */
public final class Attendance {

    private final boolean hasAttended;
    private final LocalDate recordedDate;

    /**
     * Creates an attendance record.
     */
    public Attendance(boolean hasAttended, LocalDate recordedDate) {
        requireAllNonNull(recordedDate);
        this.hasAttended = hasAttended;
        this.recordedDate = recordedDate;
    }

    public boolean hasAttended() {
        return hasAttended;
    }

    public LocalDate getRecordedDate() {
        return recordedDate;
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
        return hasAttended == otherAttendance.hasAttended
                && recordedDate.equals(otherAttendance.recordedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hasAttended, recordedDate);
    }

    @Override
    public String toString() {
        return "Attendance{"
                + "hasAttended=" + hasAttended
                + ", recordedDate=" + recordedDate
                + "}";
    }
}
