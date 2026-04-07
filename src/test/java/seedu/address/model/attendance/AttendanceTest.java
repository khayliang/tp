package seedu.address.model.attendance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class AttendanceTest {

    @Test
    public void constructor_withLocalDate_setsRecordedAtStartOfDay() {
        LocalDate date = LocalDate.of(2026, 4, 8);
        Attendance attendance = new Attendance(true, date);

        assertTrue(attendance.hasAttended());
        assertEquals(LocalDateTime.of(2026, 4, 8, 0, 0), attendance.getRecordedAt());
    }

    @Test
    public void constructor_withNullLocalDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Attendance(true, (LocalDate) null));
    }

    @Test
    public void constructor_withNullLocalDateTime_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Attendance(true, (LocalDateTime) null));
    }

    @Test
    public void equals() {
        Attendance attendedMorning = new Attendance(true, LocalDateTime.of(2026, 4, 8, 8, 0));
        Attendance attendedMorningCopy = new Attendance(true, LocalDateTime.of(2026, 4, 8, 8, 0));
        Attendance missedMorning = new Attendance(false, LocalDateTime.of(2026, 4, 8, 8, 0));
        Attendance attendedAfternoon = new Attendance(true, LocalDateTime.of(2026, 4, 8, 15, 0));

        assertTrue(attendedMorning.equals(attendedMorning));
        assertTrue(attendedMorning.equals(attendedMorningCopy));
        assertFalse(attendedMorning.equals(missedMorning));
        assertFalse(attendedMorning.equals(attendedAfternoon));
        assertFalse(attendedMorning.equals(null));
        assertFalse(attendedMorning.equals(1));
    }
}
