package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LAST_ATTENDANCE;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditAttdCommand.
 */
public class EditAttdCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredListWithDatetime_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        LocalDateTime lastAttendance = LocalDateTime.parse(VALID_LAST_ATTENDANCE);
        EditAttdCommand editCommand = new EditAttdCommand(INDEX_FIRST_PERSON, lastAttendance);

        Person editedPerson = new PersonBuilder(personToEdit)
            .withLastAttendance(VALID_LAST_ATTENDANCE)
            .build();
        String expectedMessage = String.format(EditAttdCommand.MESSAGE_EDIT_ATTD_SUCCESS,
                Messages.format(editedPerson), lastAttendance.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(editCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_validIndexUnfilteredListWithExplicitDatetime_success() throws CommandException {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        LocalDateTime fixedTime = LocalDateTime.parse("2026-03-15T10:00:00");
        EditAttdCommand editCommand = new EditAttdCommand(INDEX_FIRST_PERSON, fixedTime);

        editCommand.execute(model);

        Person editedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        LocalDateTime recordedAttendance = editedPerson.getLastAttendance().orElseThrow();

        assertEquals(fixedTime, recordedAttendance);
        assertEquals(personToEdit.getName(), editedPerson.getName());
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        LocalDateTime anyTime = LocalDateTime.parse(VALID_LAST_ATTENDANCE);
        EditAttdCommand editCommand = new EditAttdCommand(outOfBoundIndex, anyTime);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        LocalDateTime lastAttendance = LocalDateTime.parse(VALID_LAST_ATTENDANCE);
        EditAttdCommand standardCommand = new EditAttdCommand(INDEX_FIRST_PERSON, lastAttendance);

        // same values -> returns true
        EditAttdCommand commandWithSameValues = new EditAttdCommand(INDEX_FIRST_PERSON, lastAttendance);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different type -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditAttdCommand(INDEX_SECOND_PERSON, lastAttendance)));

        // different attendance date-time -> returns false
        LocalDateTime differentLastAttendance = LocalDateTime.parse("2026-02-01T10:00:00");
        assertFalse(standardCommand.equals(new EditAttdCommand(INDEX_FIRST_PERSON,
                differentLastAttendance)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        LocalDateTime lastAttendance = LocalDateTime.parse(VALID_LAST_ATTENDANCE);
        EditAttdCommand editCommand = new EditAttdCommand(index, lastAttendance);
        String expected = EditAttdCommand.class.getCanonicalName()
                + "{index=" + index + ", attendanceToSet=" + lastAttendance + "}";
        assertEquals(expected, editCommand.toString());
    }
}
