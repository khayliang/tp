package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.attendance.Attendance;
import seedu.address.model.attendance.AttendanceHistory;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;
import seedu.address.model.person.Phone;
import seedu.address.model.recurrence.Recurrence;
import seedu.address.model.session.Appointment;
import seedu.address.model.session.ScheduledSession;

/**
 * Contains integration tests and unit tests for DeleteAttdCommand.
 */
public class DeleteAttdCommandTest {

    private static final LocalDateTime START = LocalDateTime.parse("2026-01-01T10:00:00");
    private static final LocalDateTime NEXT = LocalDateTime.parse("2026-01-15T10:00:00");
    private static final LocalDateTime FIRST_ATTENDANCE = LocalDateTime.parse("2026-01-01T10:00:00");
    private static final LocalDateTime LATEST_ATTENDANCE = LocalDateTime.parse("2026-01-08T10:00:00");

    private final Model model = new ModelManager(new AddressBook(), new UserPrefs());

    @Test
    public void execute_deleteLatestDateTime_rollsBackRecurrence() {
        Person personToEdit = createPersonWithRecurringSession();
        model.addPerson(personToEdit);

        DeleteAttdCommand deleteCommand =
                new DeleteAttdCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, LATEST_ATTENDANCE);

        ScheduledSession expectedSession = new ScheduledSession(
                Recurrence.WEEKLY,
                START,
                LocalDateTime.parse("2026-01-08T10:00:00"),
                new AttendanceHistory(new Attendance(true, FIRST_ATTENDANCE)),
                "Algebra");
        Person editedPerson = new PersonBuilder(personToEdit)
                .withAppointment(new Appointment(List.of(expectedSession)))
                .build();

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        String deletionFeedback = String.format(DeleteAttdCommand.MESSAGE_DELETE_ATTD_SUCCESS,
                Messages.format(editedPerson), 1, "2026-01-08T10:00:00");
        String rollbackFeedback = String.format(DeleteAttdCommand.MESSAGE_RECURRING_NEXT_ROLLED_BACK,
                "2026-01-15T10:00:00", "2026-01-08T10:00:00");
        String expectedMessage = deletionFeedback + " " + rollbackFeedback;
        assertCommandSuccess(deleteCommand, model,
                new CommandResult(expectedMessage, editedPerson), expectedModel);

        LocalDateTime updatedNext = model.getFilteredPersonList().get(0).getAppointments().get(0).getNext();
        assertEquals(LocalDateTime.parse("2026-01-08T10:00:00"), updatedNext);
    }

    @Test
    public void execute_deleteNonLatestDate_doesNotRollbackRecurrence() throws Exception {
        Person personToEdit = createPersonWithRecurringSession();
        model.addPerson(personToEdit);

        DeleteAttdCommand deleteCommand =
                new DeleteAttdCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, LocalDate.parse("2026-01-01"));

        deleteCommand.execute(model);

        ScheduledSession updatedSession = model.getFilteredPersonList().get(0).getAppointments().get(0);
        assertEquals(NEXT, updatedSession.getNext());
        assertEquals(1, updatedSession.getAttendanceHistory().getRecords().size());
        assertTrue(updatedSession.getAttendanceHistory().hasRecordAt(LATEST_ATTENDANCE));
    }

    @Test
    public void execute_attendanceNotFound_failure() {
        Person personToEdit = createPersonWithRecurringSession();
        model.addPerson(personToEdit);

        DeleteAttdCommand deleteCommand =
                new DeleteAttdCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, LocalDate.parse("2026-02-01"));

        assertCommandFailure(deleteCommand, model,
                String.format(DeleteAttdCommand.MESSAGE_ATTENDANCE_NOT_FOUND, "2026-02-01"));
    }

    @Test
    public void execute_invalidSessionIndex_failure() {
        Person personToEdit = createPersonWithRecurringSession();
        model.addPerson(personToEdit);

        DeleteAttdCommand deleteCommand =
                new DeleteAttdCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON, LocalDate.parse("2026-01-08"));

        assertCommandFailure(deleteCommand, model, DeleteAttdCommand.MESSAGE_INVALID_APPOINTMENT_INDEX);
    }

    @Test
    public void execute_invalidPersonIndex_failure() {
        Person personToEdit = createPersonWithRecurringSession();
        model.addPerson(personToEdit);

        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteAttdCommand deleteCommand =
                new DeleteAttdCommand(outOfBoundIndex, INDEX_FIRST_PERSON, LocalDate.parse("2026-01-08"));

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        DeleteAttdCommand firstCommand =
                new DeleteAttdCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, LATEST_ATTENDANCE);
        DeleteAttdCommand secondCommand =
                new DeleteAttdCommand(INDEX_SECOND_PERSON, INDEX_FIRST_PERSON, LATEST_ATTENDANCE);

        assertTrue(firstCommand.equals(firstCommand));
        assertTrue(firstCommand.equals(
                new DeleteAttdCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, LATEST_ATTENDANCE)));
        assertFalse(firstCommand.equals(1));
        assertFalse(firstCommand.equals(null));
        assertFalse(firstCommand.equals(secondCommand));
        assertFalse(firstCommand.equals(
                new DeleteAttdCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, LocalDate.parse("2026-01-08"))));
    }

    @Test
    public void toStringMethod() {
        DeleteAttdCommand deleteCommand =
                new DeleteAttdCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, LATEST_ATTENDANCE);
        String expected = DeleteAttdCommand.class.getCanonicalName()
                + "{personIndex=" + INDEX_FIRST_PERSON
                + ", sessionIndex=" + INDEX_FIRST_PERSON
                + ", recordedAt=" + LATEST_ATTENDANCE
                + ", recordedDate=null}";
        assertEquals(expected, deleteCommand.toString());
    }

    private Person createPersonWithRecurringSession() {
        AttendanceHistory history = new AttendanceHistory(
                new Attendance(true, FIRST_ATTENDANCE),
                new Attendance(true, LATEST_ATTENDANCE));
        ScheduledSession session = new ScheduledSession(Recurrence.WEEKLY, START, NEXT, history, "Algebra");
        return new PersonBuilder(
                new Name("Alex"),
                new Phone("90010001"),
                new Email("alex@example.com"),
                new Address("Alex Street 1"),
                java.util.Set.of())
                .withAppointment(new Appointment(List.of(session)))
                .build();
    }
}
