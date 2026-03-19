package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_START;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

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

        Person editedPerson = new Person(personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                personToEdit.getAddress(), personToEdit.getTags(), personToEdit.getSubjects(),
                personToEdit.getParentName(), Optional.of(appointmentStart), personToEdit.getPaymentDate());

        String expectedMessage = String.format(EditApptCommand.MESSAGE_EDIT_APPT_SUCCESS,
                editedPerson.getName().fullName, appointmentStart.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        LocalDateTime appointmentStart = LocalDateTime.parse(VALID_APPOINTMENT_START);
        EditApptCommand editCommand = new EditApptCommand(outOfBoundIndex, appointmentStart);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
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
