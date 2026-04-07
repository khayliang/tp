package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.academic.Academics;
import seedu.address.model.academic.Level;
import seedu.address.model.academic.Subject;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditAcademicsCommand.
 */
public class EditAcademicsCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_clearAcademics_success() {
        Person personInList = model.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased());

        EditAcademicsCommand.EditAcademicsDescriptor descriptor =
                new EditAcademicsCommand.EditAcademicsDescriptor();
        descriptor.setSubjects(new HashSet<>()); // clear

        EditAcademicsCommand editCommand =
                new EditAcademicsCommand(INDEX_FIRST_PERSON, descriptor);

        Person editedPerson = new PersonBuilder(personInList)
                .withAcademics(new Academics(new HashSet<>()))
                .build();

        String expectedMessage = String.format(
                EditAcademicsCommand.MESSAGE_EDIT_ACADEMICS_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(
                new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personInList, editedPerson);

        CommandResult expectedCommandResult =
                new CommandResult(expectedMessage, editedPerson);

        assertCommandSuccess(editCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_editNoteOnly_success() {
        Person personInList = model.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased());

        EditAcademicsCommand.EditAcademicsDescriptor descriptor =
                new EditAcademicsCommand.EditAcademicsDescriptor();
        descriptor.setNote("Good progress");

        EditAcademicsCommand editCommand =
                new EditAcademicsCommand(INDEX_FIRST_PERSON, descriptor);

        Person editedPerson = new PersonBuilder(personInList)
                .withAcademics(new Academics(
                        personInList.getAcademics().getSubjects(),
                        java.util.Optional.of("Good progress")))
                .build();

        Model expectedModel = new ModelManager(
                new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personInList, editedPerson);

        assertCommandSuccess(editCommand, model,
                new CommandResult(String.format(
                        EditAcademicsCommand.MESSAGE_EDIT_ACADEMICS_SUCCESS,
                        Messages.format(editedPerson)), editedPerson),
                expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(
                model.getFilteredPersonList().size() + 1);

        EditAcademicsCommand.EditAcademicsDescriptor descriptor =
                new EditAcademicsCommand.EditAcademicsDescriptor();
        descriptor.setSubjects(Set.of(new Subject("Math", Level.STRONG)));

        EditAcademicsCommand editCommand =
                new EditAcademicsCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model,
                Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        EditAcademicsCommand.EditAcademicsDescriptor descriptor1 =
                new EditAcademicsCommand.EditAcademicsDescriptor();
        descriptor1.setSubjects(Set.of(new Subject("Math", Level.STRONG)));

        EditAcademicsCommand.EditAcademicsDescriptor descriptor2 =
                new EditAcademicsCommand.EditAcademicsDescriptor();
        descriptor2.setSubjects(Set.of(new Subject("Physics", Level.BASIC)));

        final EditAcademicsCommand standardCommand =
                new EditAcademicsCommand(INDEX_FIRST_PERSON, descriptor1);

        // same values
        EditAcademicsCommand commandWithSameValues =
                new EditAcademicsCommand(INDEX_FIRST_PERSON, descriptor1);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object
        assertTrue(standardCommand.equals(standardCommand));

        // null
        assertFalse(standardCommand.equals(null));

        // different type
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index
        assertFalse(standardCommand.equals(
                new EditAcademicsCommand(INDEX_SECOND_PERSON, descriptor1)));

        // different descriptor
        assertFalse(standardCommand.equals(
                new EditAcademicsCommand(INDEX_FIRST_PERSON, descriptor2)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);

        EditAcademicsCommand.EditAcademicsDescriptor descriptor =
                new EditAcademicsCommand.EditAcademicsDescriptor();
        descriptor.setSubjects(Set.of(new Subject("Math", Level.STRONG)));

        EditAcademicsCommand command =
                new EditAcademicsCommand(index, descriptor);

        String expected = EditAcademicsCommand.class.getCanonicalName()
                + "{index=" + index + ", descriptor=" + descriptor + "}";

        assertEquals(expected, command.toString());
    }
}
