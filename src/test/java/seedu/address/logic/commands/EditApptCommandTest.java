package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_START;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.ListDisplayMode;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.AppointmentInWeekPredicate;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditApptCommand.
 */
public class EditApptCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        LocalDateTime appointmentStart = LocalDateTime.parse(VALID_APPOINTMENT_START);
        EditApptCommand editCommand = new EditApptCommand(INDEX_FIRST_PERSON, appointmentStart);

        Person editedPerson = new PersonBuilder(personToEdit)
                .withAppointmentStart(VALID_APPOINTMENT_START)
                .build();

        String expectedMessage = String.format(EditApptCommand.MESSAGE_EDIT_APPT_SUCCESS,
                Messages.format(editedPerson), appointmentStart.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(editCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        LocalDateTime appointmentStart = LocalDateTime.parse(VALID_APPOINTMENT_START);
        EditApptCommand editCommand = new EditApptCommand(outOfBoundIndex, appointmentStart);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexAppointmentDisplayMode_success() {
        Person laterAppointmentPerson = new PersonBuilder().withName("Later Appointment")
                .withPhone("90000001").withEmail("later@example.com").withAddress("Later Street 1")
                .withAppointmentStart("2026-01-20T10:00:00").build();
        Person earlierAppointmentPerson = new PersonBuilder().withName("Earlier Appointment")
                .withPhone("90000002").withEmail("earlier@example.com").withAddress("Earlier Street 2")
                .withAppointmentStart("2026-01-10T10:00:00").build();

        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(laterAppointmentPerson);
        addressBook.addPerson(earlierAppointmentPerson);

        model = new ModelManager(addressBook, new UserPrefs());
        model.setListDisplayMode(ListDisplayMode.APPOINTMENT);

        LocalDateTime newAppointmentStart = LocalDateTime.parse(VALID_APPOINTMENT_START);
        EditApptCommand editCommand = new EditApptCommand(INDEX_FIRST_PERSON, newAppointmentStart);

        Person editedPerson = new PersonBuilder(earlierAppointmentPerson)
                .withAppointmentStart(VALID_APPOINTMENT_START)
                .build();
        String expectedMessage = String.format(EditApptCommand.MESSAGE_EDIT_APPT_SUCCESS,
                Messages.format(editedPerson), newAppointmentStart.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        Model expectedModel = new ModelManager(new AddressBook(addressBook), new UserPrefs());
        expectedModel.setListDisplayMode(ListDisplayMode.APPOINTMENT);
        expectedModel.setPerson(earlierAppointmentPerson, editedPerson);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(editCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_validIndexFilteredListPersonDisplayMode_success() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        showPersonAtIndex(model, INDEX_SECOND_PERSON);
        model.setListDisplayMode(ListDisplayMode.PERSON);

        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        LocalDateTime appointmentStart = LocalDateTime.parse(VALID_APPOINTMENT_START);
        EditApptCommand editCommand = new EditApptCommand(INDEX_FIRST_PERSON, appointmentStart);

        Person editedPerson = new PersonBuilder(personToEdit)
                .withAppointmentStart(VALID_APPOINTMENT_START)
                .build();
        String expectedMessage = String.format(EditApptCommand.MESSAGE_EDIT_APPT_SUCCESS,
                Messages.format(editedPerson), appointmentStart.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        showPersonAtIndex(expectedModel, INDEX_SECOND_PERSON);
        expectedModel.setListDisplayMode(ListDisplayMode.PERSON);
        expectedModel.setPerson(personToEdit, editedPerson);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(editCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_validIndexFilteredListAppointmentDisplayMode_success() {
        Person laterAppointmentPerson = new PersonBuilder().withName("Later Appointment")
                .withPhone("90000101").withEmail("later2@example.com").withAddress("Later Street 2")
                .withAppointmentStart("2026-01-20T10:00:00").build();
        Person earlierAppointmentPerson = new PersonBuilder().withName("Earlier Appointment")
                .withPhone("90000102").withEmail("earlier2@example.com").withAddress("Earlier Street 3")
                .withAppointmentStart("2026-01-19T10:00:00").build();
        Person outOfWeekAppointmentPerson = new PersonBuilder().withName("Out Of Week Appointment")
                .withPhone("90000103").withEmail("outofweek@example.com").withAddress("Out Street 1")
                .withAppointmentStart("2026-02-05T10:00:00").build();

        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(laterAppointmentPerson);
        addressBook.addPerson(earlierAppointmentPerson);
        addressBook.addPerson(outOfWeekAppointmentPerson);

        AppointmentInWeekPredicate targetWeekPredicate = new AppointmentInWeekPredicate(LocalDate.of(2026, 1, 20));
        model = new ModelManager(addressBook, new UserPrefs());
        model.updateFilteredPersonList(targetWeekPredicate);
        model.setListDisplayMode(ListDisplayMode.APPOINTMENT);

        LocalDateTime newAppointmentStart = LocalDateTime.parse("2026-01-22T08:00:00");
        EditApptCommand editCommand = new EditApptCommand(INDEX_FIRST_PERSON, newAppointmentStart);

        Person editedPerson = new PersonBuilder(earlierAppointmentPerson)
                .withAppointmentStart("2026-01-22T08:00:00")
                .build();
        String expectedMessage = String.format(EditApptCommand.MESSAGE_EDIT_APPT_SUCCESS,
                Messages.format(editedPerson), newAppointmentStart.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        Model expectedModel = new ModelManager(new AddressBook(addressBook), new UserPrefs());
        expectedModel.updateFilteredPersonList(targetWeekPredicate);
        expectedModel.setListDisplayMode(ListDisplayMode.APPOINTMENT);
        expectedModel.setPerson(earlierAppointmentPerson, editedPerson);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(editCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void equals() {
        LocalDateTime appointmentStart = LocalDateTime.parse(VALID_APPOINTMENT_START);
        EditApptCommand standardCommand = new EditApptCommand(INDEX_FIRST_PERSON, appointmentStart);

        // same values -> returns true
        EditApptCommand commandWithSameValues = new EditApptCommand(INDEX_FIRST_PERSON, appointmentStart);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // same minute but different seconds/nanoseconds -> returns true
        EditApptCommand sameMinuteCommand = new EditApptCommand(
                INDEX_FIRST_PERSON, LocalDateTime.parse("2026-01-13T08:00:59.999999"));
        assertTrue(standardCommand.equals(sameMinuteCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different type -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditApptCommand(INDEX_SECOND_PERSON, appointmentStart)));

        // different appointment start -> returns false
        LocalDateTime differentAppointmentStart = LocalDateTime.parse("2026-02-01T10:00:00");
        assertFalse(standardCommand.equals(new EditApptCommand(INDEX_FIRST_PERSON, differentAppointmentStart)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        LocalDateTime appointmentStart = LocalDateTime.parse(VALID_APPOINTMENT_START);
        EditApptCommand editCommand = new EditApptCommand(index, appointmentStart);
        String expected = EditApptCommand.class.getCanonicalName()
                + "{index=" + index + ", appointmentStart=" + appointmentStart + "}";
        assertEquals(expected, editCommand.toString());
    }
}
