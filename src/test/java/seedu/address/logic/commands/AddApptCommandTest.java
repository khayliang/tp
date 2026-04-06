package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_DESCRIPTION;
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
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.attendance.AttendanceRecords;
import seedu.address.model.person.AppointmentInWeekPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.recurrence.Recurrence;
import seedu.address.model.session.Appointment;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests and unit tests for AddApptCommand.
 */
public class AddApptCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        LocalDateTime appointmentStart = LocalDateTime.parse(VALID_APPOINTMENT_START);
        AddApptCommand addCommand = new AddApptCommand(INDEX_FIRST_PERSON, appointmentStart,
                Recurrence.NONE, VALID_APPOINTMENT_DESCRIPTION);

        Person editedPerson = new PersonBuilder(personToEdit)
                .withAppointment(VALID_APPOINTMENT_START, VALID_APPOINTMENT_DESCRIPTION, Recurrence.NONE)
                .build();

        String expectedMessage = String.format(AddApptCommand.MESSAGE_ADD_APPT_SUCCESS,
                Messages.format(editedPerson), appointmentStart.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(addCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        AddApptCommand addCommand = new AddApptCommand(outOfBoundIndex, LocalDateTime.parse(VALID_APPOINTMENT_START),
                Recurrence.NONE, VALID_APPOINTMENT_DESCRIPTION);

        assertCommandFailure(addCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_duplicateAppointmentDescription_failureAndAppointmentUnchanged() {
        Person personWithAppointment = new PersonBuilder()
                .withName("Duplicate Appointment")
                .withPhone("90000003")
                .withEmail("duplicate@example.com")
                .withAddress("Duplicate Street 3")
                .withAppointment("2026-01-20T10:00:00", VALID_APPOINTMENT_DESCRIPTION, Recurrence.NONE)
                .build();

        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(personWithAppointment);
        model = new ModelManager(addressBook, new UserPrefs());

        AddApptCommand addCommand = new AddApptCommand(INDEX_FIRST_PERSON,
                LocalDateTime.parse("2026-01-21T10:00:00"),
                Recurrence.WEEKLY,
                "  " + VALID_APPOINTMENT_DESCRIPTION + "  ");

        String expectedFailureMessage = String.format(AddApptCommand.MESSAGE_DUPLICATE_APPOINTMENT_DESCRIPTION,
                VALID_APPOINTMENT_DESCRIPTION, Messages.format(personWithAppointment));

        assertCommandFailure(addCommand, model, expectedFailureMessage);
    }

    @Test
    public void execute_validIndexFilteredAppointmentResults_success() {
        Person laterAppointmentPerson = new PersonBuilder().withName("Later Appointment")
                .withPhone("90000001").withEmail("later@example.com").withAddress("Later Street 1")
                .withAppointment("2026-01-20T10:00:00", "Later lesson", Recurrence.NONE).build();
        Person earlierAppointmentPerson = new PersonBuilder().withName("Earlier Appointment")
                .withPhone("90000002").withEmail("earlier@example.com").withAddress("Earlier Street 2")
                .withAppointment("2026-01-10T10:00:00", "Earlier lesson", Recurrence.NONE).build();

        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(laterAppointmentPerson);
        addressBook.addPerson(earlierAppointmentPerson);

        model = new ModelManager(addressBook, new UserPrefs());
        model.updateFilteredPersonList(new AppointmentInWeekPredicate(LocalDate.of(2026, 1, 20)));

        LocalDateTime newAppointmentStart = LocalDateTime.parse(VALID_APPOINTMENT_START);
        AddApptCommand addCommand = new AddApptCommand(INDEX_FIRST_PERSON, newAppointmentStart,
                Recurrence.NONE, VALID_APPOINTMENT_DESCRIPTION);

        Person editedPerson = new PersonBuilder(laterAppointmentPerson)
                .addAppointment(new Appointment(Recurrence.NONE, newAppointmentStart, newAppointmentStart,
                        AttendanceRecords.EMPTY, VALID_APPOINTMENT_DESCRIPTION))
                .build();
        String expectedMessage = String.format(AddApptCommand.MESSAGE_ADD_APPT_SUCCESS,
                Messages.format(editedPerson), newAppointmentStart.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        Model expectedModel = new ModelManager(new AddressBook(addressBook), new UserPrefs());
        expectedModel.updateFilteredPersonList(new AppointmentInWeekPredicate(LocalDate.of(2026, 1, 20)));
        expectedModel.setPerson(laterAppointmentPerson, editedPerson);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(addCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_validIndexSingleItemFilteredList_success() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        showPersonAtIndex(model, INDEX_SECOND_PERSON);

        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        LocalDateTime appointmentStart = LocalDateTime.parse(VALID_APPOINTMENT_START);
        AddApptCommand addCommand = new AddApptCommand(INDEX_FIRST_PERSON, appointmentStart,
                Recurrence.NONE, VALID_APPOINTMENT_DESCRIPTION);

        Person editedPerson = new PersonBuilder(personToEdit)
                .withAppointment(VALID_APPOINTMENT_START, VALID_APPOINTMENT_DESCRIPTION, Recurrence.NONE)
                .build();
        String expectedMessage = String.format(AddApptCommand.MESSAGE_ADD_APPT_SUCCESS,
                Messages.format(editedPerson), appointmentStart.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        showPersonAtIndex(expectedModel, INDEX_SECOND_PERSON);
        expectedModel.setPerson(personToEdit, editedPerson);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(addCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_validIndexAppointmentFilteredList_success() {
        Person laterAppointmentPerson = new PersonBuilder().withName("Later Appointment")
                .withPhone("90000101").withEmail("later2@example.com").withAddress("Later Street 2")
                .withAppointment("2026-01-20T10:00:00", "Later lesson", Recurrence.NONE).build();
        Person earlierAppointmentPerson = new PersonBuilder().withName("Earlier Appointment")
                .withPhone("90000102").withEmail("earlier2@example.com").withAddress("Earlier Street 3")
                .withAppointment("2026-01-19T10:00:00", "Earlier lesson", Recurrence.NONE).build();
        Person outOfWeekAppointmentPerson = new PersonBuilder().withName("Out Of Week Appointment")
                .withPhone("90000103").withEmail("outofweek@example.com").withAddress("Out Street 1")
                .withAppointment("2026-02-05T10:00:00", "Out of week", Recurrence.NONE).build();

        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(laterAppointmentPerson);
        addressBook.addPerson(earlierAppointmentPerson);
        addressBook.addPerson(outOfWeekAppointmentPerson);

        AppointmentInWeekPredicate targetWeekPredicate = new AppointmentInWeekPredicate(LocalDate.of(2026, 1, 20));
        model = new ModelManager(addressBook, new UserPrefs());
        model.updateFilteredPersonList(targetWeekPredicate);

        LocalDateTime newAppointmentStart = LocalDateTime.parse("2026-01-22T08:00:00");
        AddApptCommand addCommand = new AddApptCommand(INDEX_FIRST_PERSON, newAppointmentStart,
                Recurrence.NONE, VALID_APPOINTMENT_DESCRIPTION);

        Person editedPerson = new PersonBuilder(laterAppointmentPerson)
                .addAppointment(new Appointment(Recurrence.NONE, newAppointmentStart, newAppointmentStart,
                        AttendanceRecords.EMPTY, VALID_APPOINTMENT_DESCRIPTION))
                .build();
        String expectedMessage = String.format(AddApptCommand.MESSAGE_ADD_APPT_SUCCESS,
                Messages.format(editedPerson), newAppointmentStart.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        Model expectedModel = new ModelManager(new AddressBook(addressBook), new UserPrefs());
        expectedModel.updateFilteredPersonList(targetWeekPredicate);
        expectedModel.setPerson(laterAppointmentPerson, editedPerson);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(addCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void equals() {
        LocalDateTime appointmentStart = LocalDateTime.parse(VALID_APPOINTMENT_START);
        AddApptCommand standardCommand = new AddApptCommand(INDEX_FIRST_PERSON, appointmentStart,
                Recurrence.NONE, VALID_APPOINTMENT_DESCRIPTION);

        AddApptCommand commandWithSameValues = new AddApptCommand(INDEX_FIRST_PERSON, appointmentStart,
                Recurrence.NONE, VALID_APPOINTMENT_DESCRIPTION);
        assertTrue(standardCommand.equals(commandWithSameValues));
        assertTrue(standardCommand.equals(standardCommand));
        assertFalse(standardCommand.equals(null));
        assertFalse(standardCommand.equals(new ClearCommand()));
        assertFalse(standardCommand.equals(new AddApptCommand(INDEX_SECOND_PERSON, appointmentStart,
                Recurrence.NONE, VALID_APPOINTMENT_DESCRIPTION)));
        assertFalse(standardCommand.equals(new AddApptCommand(INDEX_FIRST_PERSON,
                LocalDateTime.parse("2026-02-01T10:00:00"), Recurrence.NONE,
                VALID_APPOINTMENT_DESCRIPTION)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        LocalDateTime appointmentStart = LocalDateTime.parse(VALID_APPOINTMENT_START);
        AddApptCommand addCommand = new AddApptCommand(index, appointmentStart,
                Recurrence.NONE, VALID_APPOINTMENT_DESCRIPTION);
        String expected = AddApptCommand.class.getCanonicalName()
                + "{index=" + index + ", appointmentStart=" + appointmentStart + ", recurrence=NONE, "
                + "description=" + VALID_APPOINTMENT_DESCRIPTION + "}";
        assertEquals(expected, addCommand.toString());
    }
}
