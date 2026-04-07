package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getPersonBuilder;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.attendance.AttendanceHistory;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;
import seedu.address.model.recurrence.Recurrence;
import seedu.address.model.session.Appointment;

/**
 * Contains integration tests and unit tests for {@code DeleteApptCommand}.
 */
public class DeleteApptCommandTest {

    private Model model = buildModelWithAppointments();

    @Test
    public void execute_validIndexesUnfilteredList_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteApptCommand deleteCommand = new DeleteApptCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);

        Person editedPerson = new PersonBuilder(personToEdit)
                .withAppointments(personToEdit.getAppointments().subList(0, 1))
                .build();
        String expectedMessage = String.format(DeleteApptCommand.MESSAGE_DELETE_APPT_SUCCESS,
                Messages.format(editedPerson), "2026-01-20T10:00:00");

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(deleteCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundPersonIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteApptCommand deleteCommand = new DeleteApptCommand(outOfBoundPersonIndex, INDEX_FIRST_PERSON);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidAppointmentIndexUnfilteredList_failure() {
        DeleteApptCommand deleteCommand = new DeleteApptCommand(INDEX_FIRST_PERSON, Index.fromOneBased(3));

        assertCommandFailure(deleteCommand, model, DeleteApptCommand.MESSAGE_INVALID_APPOINTMENT_INDEX);
    }

    @Test
    public void execute_validIndexesFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteApptCommand deleteCommand = new DeleteApptCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);

        Person editedPerson = new PersonBuilder(personToEdit)
                .withAppointments(personToEdit.getAppointments().subList(0, 1))
                .build();
        String expectedMessage = String.format(DeleteApptCommand.MESSAGE_DELETE_APPT_SUCCESS,
                Messages.format(editedPerson), "2026-01-20T10:00:00");

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);
        expectedModel.setPerson(personToEdit, editedPerson);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(deleteCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_validIndexesSingleItemFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteApptCommand deleteCommand = new DeleteApptCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);

        Person editedPerson = new PersonBuilder(personToEdit)
                .withAppointments(personToEdit.getAppointments().subList(0, 1))
                .build();
        String expectedMessage = String.format(DeleteApptCommand.MESSAGE_DELETE_APPT_SUCCESS,
                Messages.format(editedPerson), "2026-01-20T10:00:00");

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);
        expectedModel.setPerson(personToEdit, editedPerson);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(deleteCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void equals() {
        DeleteApptCommand firstCommand = new DeleteApptCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON);
        DeleteApptCommand secondCommand = new DeleteApptCommand(INDEX_SECOND_PERSON, INDEX_FIRST_PERSON);

        assertTrue(firstCommand.equals(firstCommand));
        assertTrue(firstCommand.equals(new DeleteApptCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON)));
        assertFalse(firstCommand.equals(1));
        assertFalse(firstCommand.equals(null));
        assertFalse(firstCommand.equals(secondCommand));
        assertFalse(firstCommand.equals(new DeleteApptCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON)));
    }

    @Test
    public void toStringMethod() {
        DeleteApptCommand deleteCommand = new DeleteApptCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);
        String expected = DeleteApptCommand.class.getCanonicalName()
                + "{personIndex=" + INDEX_FIRST_PERSON + ", appointmentIndex=" + INDEX_SECOND_PERSON + "}";
        assertEquals(expected, deleteCommand.toString());
    }

    private static Model buildModelWithAppointments() {
        AddressBook addressBook = new AddressBook();
        Person personWithTwoAppointments = getPersonBuilder("Alex", "90010001",
                "alex@example.com", "Alex Street 1")
                .withAppointment(Appointment.of("2026-01-10T10:00:00", "First lesson", Recurrence.NONE))
                .addAppointment(new Appointment(Recurrence.NONE,
                        LocalDateTime.parse("2026-01-20T10:00:00"),
                        LocalDateTime.parse("2026-01-20T10:00:00"),
                        AttendanceHistory.EMPTY, "Second lesson"))
                .build();
        Person otherPerson = getPersonBuilder("Blake", "90010002",
                "blake@example.com", "Blake Street 2")
                .withAppointment(Appointment.of("2026-01-30T10:00:00", "Other lesson", Recurrence.NONE))
                .build();
        addressBook.addPerson(personWithTwoAppointments);
        addressBook.addPerson(otherPerson);
        return new ModelManager(addressBook, new UserPrefs());
    }
}
