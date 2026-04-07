package seedu.address.model.attendance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

public class AttendanceHistoryTest {

    private static final Attendance FIRST_RECORD =
            new Attendance(true, LocalDateTime.of(2026, 4, 8, 8, 0));
    private static final Attendance SECOND_RECORD =
            new Attendance(false, LocalDateTime.of(2026, 4, 8, 10, 0));
    private static final Attendance THIRD_RECORD =
            new Attendance(true, LocalDateTime.of(2026, 4, 9, 9, 0));

    @Test
    public void addAttendance_returnsNewHistory_withoutMutatingOriginal() {
        AttendanceHistory original = AttendanceHistory.EMPTY;

        AttendanceHistory updated = original.addAttendance(FIRST_RECORD);

        assertTrue(original.isEmpty());
        assertEquals(List.of(FIRST_RECORD), updated.getRecords());
    }

    @Test
    public void removeAttendance_byDate_removesAllRecordsOnDate() {
        AttendanceHistory history = new AttendanceHistory(FIRST_RECORD, SECOND_RECORD, THIRD_RECORD);

        AttendanceHistory updated = history.removeAttendance(LocalDate.of(2026, 4, 8));

        assertEquals(List.of(THIRD_RECORD), updated.getRecords());
    }

    @Test
    public void removeAttendance_byRecordedAt_removesExactRecordOnly() {
        AttendanceHistory history = new AttendanceHistory(FIRST_RECORD, SECOND_RECORD, THIRD_RECORD);

        AttendanceHistory updated = history.removeAttendance(LocalDateTime.of(2026, 4, 8, 8, 0));

        assertEquals(List.of(SECOND_RECORD, THIRD_RECORD), updated.getRecords());
    }

    @Test
    public void hasRecordOnAndAt_returnsExpectedValues() {
        AttendanceHistory history = new AttendanceHistory(FIRST_RECORD, THIRD_RECORD);

        assertTrue(history.hasRecordOn(LocalDate.of(2026, 4, 8)));
        assertFalse(history.hasRecordOn(LocalDate.of(2026, 4, 7)));

        assertTrue(history.hasRecordAt(LocalDateTime.of(2026, 4, 8, 8, 0)));
        assertFalse(history.hasRecordAt(LocalDateTime.of(2026, 4, 8, 9, 0)));
    }

    @Test
    public void getLastRecordAndLatestRecordByRecordedAt_behaveAsSpecified() {
        Attendance laterInsertedFirst = new Attendance(true, LocalDateTime.of(2026, 4, 8, 10, 0));
        Attendance earlierInsertedLast = new Attendance(true, LocalDateTime.of(2026, 4, 8, 8, 0));
        AttendanceHistory history = new AttendanceHistory(laterInsertedFirst, earlierInsertedLast);

        assertEquals(earlierInsertedLast, history.getLastRecord().orElseThrow());
        assertEquals(laterInsertedFirst, history.getLatestRecordByRecordedAt().orElseThrow());

        AttendanceHistory empty = AttendanceHistory.EMPTY;
        assertTrue(empty.getLastRecord().isEmpty());
        assertTrue(empty.getLatestRecordByRecordedAt().isEmpty());
    }

    @Test
    public void getRecords_returnsUnmodifiableList() {
        AttendanceHistory history = new AttendanceHistory(FIRST_RECORD);

        assertThrows(UnsupportedOperationException.class,
                () -> history.getRecords().add(SECOND_RECORD));
    }

    @Test
    public void methods_withNullInput_throwsNullPointerException() {
        AttendanceHistory history = new AttendanceHistory(FIRST_RECORD);

        assertThrows(NullPointerException.class, () -> history.addAttendance(null));
        assertThrows(NullPointerException.class, () -> history.removeAttendance((LocalDate) null));
        assertThrows(NullPointerException.class, () -> history.removeAttendance((LocalDateTime) null));
        assertThrows(NullPointerException.class, () -> history.hasRecordOn(null));
        assertThrows(NullPointerException.class, () -> history.hasRecordAt(null));
    }
}
