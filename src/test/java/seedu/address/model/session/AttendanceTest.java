package seedu.address.model.session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

public class AttendanceTest {

    @Test
    public void addAttendance_duplicateTimestamp_deduplicated() {
        LocalDateTime time = LocalDateTime.parse("2026-03-20T17:00:00");

        Attendance attendance = Attendance.EMPTY
                .addAttendance(time)
                .addAttendance(time);

        assertEquals(1, attendance.getHistory().size());
        assertEquals(time, attendance.getLastAttendance().orElseThrow());
    }

    @Test
    public void addAttendance_sameMinuteDifferentSeconds_deduplicated() {
        LocalDateTime first = LocalDateTime.parse("2026-03-20T17:00:05");
        LocalDateTime second = LocalDateTime.parse("2026-03-20T17:00:59.123456");

        Attendance attendance = Attendance.EMPTY
                .addAttendance(first)
                .addAttendance(second);

        assertEquals(1, attendance.getHistory().size());
        assertEquals(LocalDateTime.parse("2026-03-20T17:00:00"), attendance.getLastAttendance().orElseThrow());
    }

    @Test
    public void getHistoryDescending_returnsNewestFirst() {
        LocalDateTime older = LocalDateTime.parse("2026-03-15T10:00:00");
        LocalDateTime newer = LocalDateTime.parse("2026-03-20T17:00:00");

        Attendance attendance = Attendance.EMPTY
                .addAttendance(older)
                .addAttendance(newer);

        assertEquals(List.of(newer, older), attendance.getHistoryDescending());
    }

    @Test
    public void getHistoryOnOrAfter_filtersAndSortsDescending() {
        LocalDateTime first = LocalDateTime.parse("2026-03-15T10:00:00");
        LocalDateTime second = LocalDateTime.parse("2026-03-20T17:00:00");
        LocalDateTime third = LocalDateTime.parse("2026-03-23T09:30:00");

        Attendance attendance = Attendance.EMPTY
                .addAttendance(first)
                .addAttendance(second)
                .addAttendance(third);

        assertEquals(List.of(third, second), attendance.getHistoryOnOrAfter(second));
    }

    @Test
    public void addAttendance_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> Attendance.EMPTY.addAttendance(null));
    }

    @Test
    public void emptyAttendance_lastAttendanceIsEmpty() {
        assertTrue(Attendance.EMPTY.getLastAttendance().isEmpty());
    }
}
