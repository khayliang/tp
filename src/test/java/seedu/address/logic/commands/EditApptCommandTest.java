package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
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
 * Contains integration tests and unit tests for EditApptCommand.
 */
public class EditApptCommandTest {

    private final Model model = new ModelManager(new AddressBook(), new UserPrefs());

    @Test
    public void execute_editDate_updatesStartAndNext() {
        Person personToEdit = createPersonWithSingleSession();
        model.addPerson(personToEdit);

        LocalDateTime editedDateTime = LocalDateTime.parse("2026-02-01T09:30:00");
        EditApptCommand editCommand = new EditApptCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                Optional.of(editedDateTime), Optional.empty(), Optional.empty());

        ScheduledSession expectedSession = new ScheduledSession(
                Recurrence.WEEKLY,
                editedDateTime,
                editedDateTime,
                AttendanceHistory.EMPTY,
                "Algebra");
        Person editedPerson = new PersonBuilder(personToEdit)
                .withAppointment(new Appointment(List.of(expectedSession)))
                .build();

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        String expectedMessage = String.format(EditApptCommand.MESSAGE_EDIT_APPT_SUCCESS,
                Messages.format(editedPerson), "2026-02-01T09:30:00 (WEEKLY, Algebra)");
        assertCommandSuccess(editCommand, model, new CommandResult(expectedMessage, editedPerson), expectedModel);
    }

    @Test
    public void execute_editRecurrenceAndDescription_success() {
        Person personToEdit = createPersonWithSingleSession();
        model.addPerson(personToEdit);

        EditApptCommand editCommand = new EditApptCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                Optional.empty(), Optional.of(Recurrence.MONTHLY), Optional.of("Consultation"));

        ScheduledSession expectedSession = new ScheduledSession(
                Recurrence.MONTHLY,
                LocalDateTime.parse("2026-01-01T10:00:00"),
                LocalDateTime.parse("2026-01-01T10:00:00"),
                AttendanceHistory.EMPTY,
                "Consultation");
        Person editedPerson = new PersonBuilder(personToEdit)
                .withAppointment(new Appointment(List.of(expectedSession)))
                .build();

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        String expectedMessage = String.format(EditApptCommand.MESSAGE_EDIT_APPT_SUCCESS,
                Messages.format(editedPerson), "2026-01-01T10:00:00 (MONTHLY, Consultation)");
        assertCommandSuccess(editCommand, model, new CommandResult(expectedMessage, editedPerson), expectedModel);
    }

    @Test
    public void execute_invalidSessionIndex_failure() {
        Person personToEdit = createPersonWithSingleSession();
        model.addPerson(personToEdit);

        EditApptCommand editCommand = new EditApptCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON,
                Optional.of(LocalDateTime.parse("2026-02-01T09:30:00")), Optional.empty(), Optional.empty());

        assertCommandFailure(editCommand, model, EditApptCommand.MESSAGE_INVALID_APPOINTMENT_INDEX);
    }

    @Test
    public void execute_invalidPersonIndex_failure() {
        Person personToEdit = createPersonWithSingleSession();
        model.addPerson(personToEdit);

        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditApptCommand editCommand = new EditApptCommand(outOfBoundIndex, INDEX_FIRST_PERSON,
                Optional.of(LocalDateTime.parse("2026-02-01T09:30:00")), Optional.empty(), Optional.empty());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_duplicateAppointmentDescription_failureAndAppointmentUnchanged() {
        Person personToEdit = createPersonWithTwoSessions();
        model.addPerson(personToEdit);

        EditApptCommand editCommand = new EditApptCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON,
                Optional.empty(), Optional.empty(), Optional.of("  Algebra  "));

        String expectedFailureMessage = String.format(EditApptCommand.MESSAGE_DUPLICATE_APPOINTMENT_DESCRIPTION,
                "Algebra", Messages.format(personToEdit));

        assertCommandFailure(editCommand, model, expectedFailureMessage);
    }

    @Test
    public void equals() {
        EditApptCommand firstCommand = new EditApptCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                Optional.of(LocalDateTime.parse("2026-02-01T09:30:00")),
                Optional.of(Recurrence.WEEKLY),
                Optional.of("Algebra"));
        EditApptCommand secondCommand = new EditApptCommand(INDEX_SECOND_PERSON, INDEX_FIRST_PERSON,
                Optional.of(LocalDateTime.parse("2026-02-01T09:30:00")),
                Optional.of(Recurrence.WEEKLY),
                Optional.of("Algebra"));

        assertTrue(firstCommand.equals(firstCommand));
        assertTrue(firstCommand.equals(new EditApptCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                Optional.of(LocalDateTime.parse("2026-02-01T09:30:00")),
                Optional.of(Recurrence.WEEKLY),
                Optional.of("Algebra"))));
        assertFalse(firstCommand.equals(1));
        assertFalse(firstCommand.equals(null));
        assertFalse(firstCommand.equals(secondCommand));
    }

    @Test
    public void toStringMethod() {
        EditApptCommand editCommand = new EditApptCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                Optional.of(LocalDateTime.parse("2026-02-01T09:30:00")),
                Optional.of(Recurrence.WEEKLY),
                Optional.of("Algebra"));
        String expected = EditApptCommand.class.getCanonicalName()
                + "{personIndex=" + INDEX_FIRST_PERSON
                + ", sessionIndex=" + INDEX_FIRST_PERSON
                + ", appointmentStart=2026-02-01T09:30"
                + ", recurrence=WEEKLY"
                + ", description=Algebra}";
        assertEquals(expected, editCommand.toString());
    }

    private Person createPersonWithSingleSession() {
        ScheduledSession session = new ScheduledSession(
                Recurrence.WEEKLY,
                LocalDateTime.parse("2026-01-01T10:00:00"),
                LocalDateTime.parse("2026-01-01T10:00:00"),
                AttendanceHistory.EMPTY,
                "Algebra");
        return new PersonBuilder(
                new Name("Alex"),
                new Phone("90010001"),
                new Email("alex@example.com"),
                new Address("Alex Street 1"),
                java.util.Set.of())
                .withAppointment(new Appointment(List.of(session)))
                .build();
    }

    private Person createPersonWithTwoSessions() {
        ScheduledSession firstSession = new ScheduledSession(
                Recurrence.WEEKLY,
                LocalDateTime.parse("2026-01-01T10:00:00"),
                LocalDateTime.parse("2026-01-01T10:00:00"),
                AttendanceHistory.EMPTY,
                "Algebra");
        ScheduledSession secondSession = new ScheduledSession(
                Recurrence.NONE,
                LocalDateTime.parse("2026-01-08T10:00:00"),
                LocalDateTime.parse("2026-01-08T10:00:00"),
                AttendanceHistory.EMPTY,
                "Consultation");
        return new PersonBuilder(
                new Name("Alex"),
                new Phone("90010001"),
                new Email("alex@example.com"),
                new Address("Alex Street 1"),
                java.util.Set.of())
                .withAppointment(new Appointment(List.of(firstSession, secondSession)))
                .build();
    }
}
