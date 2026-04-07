package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.getPersonBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.attendance.AttendanceHistory;
import seedu.address.model.recurrence.Recurrence;
import seedu.address.model.session.Appointment;
import seedu.address.model.session.ScheduledSession;

public class AppointmentInWeekPredicateTest {

    @Test
    public void constructor_setsWeekRangeFromTargetDate() {
        AppointmentInWeekPredicate predicate = new AppointmentInWeekPredicate(LocalDate.of(2026, 4, 8));

        assertEquals(LocalDate.of(2026, 4, 6), predicate.getWeekStart());
        assertEquals(LocalDate.of(2026, 4, 12), predicate.getWeekEnd());
    }

    @Test
    public void test_sessionOnWeekStart_returnsTrue() {
        AppointmentInWeekPredicate predicate = new AppointmentInWeekPredicate(LocalDate.of(2026, 4, 8));
        Person person = buildPersonWithSessionNext(LocalDateTime.of(2026, 4, 6, 9, 0));

        assertTrue(predicate.test(person));
    }

    @Test
    public void test_sessionOnWeekEnd_returnsTrue() {
        AppointmentInWeekPredicate predicate = new AppointmentInWeekPredicate(LocalDate.of(2026, 4, 8));
        Person person = buildPersonWithSessionNext(LocalDateTime.of(2026, 4, 12, 23, 0));

        assertTrue(predicate.test(person));
    }

    @Test
    public void test_sessionOutsideWeek_returnsFalse() {
        AppointmentInWeekPredicate predicate = new AppointmentInWeekPredicate(LocalDate.of(2026, 4, 8));
        Person beforeWeek = buildPersonWithSessionNext(LocalDateTime.of(2026, 4, 5, 20, 0));
        Person afterWeek = buildPersonWithSessionNext(LocalDateTime.of(2026, 4, 13, 8, 0));

        assertFalse(predicate.test(beforeWeek));
        assertFalse(predicate.test(afterWeek));
    }

    @Test
    public void test_multipleSessions_anyMatchWithinWeek_returnsTrue() {
        AppointmentInWeekPredicate predicate = new AppointmentInWeekPredicate(LocalDate.of(2026, 4, 8));
        Person person = buildPersonWithSessionNext(
                LocalDateTime.of(2026, 4, 5, 9, 0),
                LocalDateTime.of(2026, 4, 9, 9, 0));

        assertTrue(predicate.test(person));
    }

    @Test
    public void equals() {
        AppointmentInWeekPredicate first = new AppointmentInWeekPredicate(LocalDate.of(2026, 4, 8));
        AppointmentInWeekPredicate sameWeek = new AppointmentInWeekPredicate(LocalDate.of(2026, 4, 10));
        AppointmentInWeekPredicate differentWeek = new AppointmentInWeekPredicate(LocalDate.of(2026, 4, 13));

        assertTrue(first.equals(first));
        assertTrue(first.equals(sameWeek));
        assertFalse(first.equals(differentWeek));
        assertFalse(first.equals(null));
        assertFalse(first.equals(1));
    }

    private Person buildPersonWithSessionNext(LocalDateTime... nextDateTimes) {
        List<ScheduledSession> sessions = new ArrayList<>();
        for (int i = 0; i < nextDateTimes.length; i++) {
            LocalDateTime next = nextDateTimes[i];
            sessions.add(new ScheduledSession(
                    Recurrence.NONE,
                    next,
                    next,
                    AttendanceHistory.EMPTY,
                    "session " + i));
        }
        Appointment appointment = new Appointment(sessions);
        return getPersonBuilder("Student One").withAppointment(appointment).build();
    }
}
