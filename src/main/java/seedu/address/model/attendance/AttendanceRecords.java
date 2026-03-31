package seedu.address.model.attendance;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Immutable attendance record list for an appointment.
 */
public final class AttendanceRecords {

    public static final AttendanceRecords EMPTY = new AttendanceRecords();

    private final List<Attendance> records;

    /**
     * Creates an empty attendance record list.
     */
    public AttendanceRecords() {
        this.records = List.of();
    }

    /**
     * Creates an attendance record list from the provided records.
     */
    public AttendanceRecords(Attendance... records) {
        requireAllNonNull((Object[]) records);
        List<Attendance> copiedRecords = new ArrayList<>();
        Collections.addAll(copiedRecords, records);
        this.records = List.copyOf(copiedRecords);
    }

    private AttendanceRecords(List<Attendance> records) {
        this.records = List.copyOf(records);
    }

    /**
     * Returns a new {@code AttendanceRecords} with {@code attendance} appended.
     */
    public AttendanceRecords addAttendance(Attendance attendance) {
        requireAllNonNull(attendance);
        List<Attendance> updatedRecords = new ArrayList<>(records);
        updatedRecords.add(attendance);
        return new AttendanceRecords(updatedRecords);
    }

    public boolean isEmpty() {
        return records.isEmpty();
    }

    public List<Attendance> getRecords() {
        return records;
    }

    public Optional<Attendance> getLastRecord() {
        if (records.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(records.get(records.size() - 1));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AttendanceRecords)) {
            return false;
        }

        AttendanceRecords otherAttendance = (AttendanceRecords) other;
        return records.equals(otherAttendance.records);
    }

    @Override
    public int hashCode() {
        return Objects.hash(records);
    }

    @Override
    public String toString() {
        return records.toString();
    }
}
