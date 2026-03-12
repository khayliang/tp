package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PARENT_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PARENT_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.ParentName;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code EditParentCommand}.
 */
public class EditParentCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        ParentName newParentName = new ParentName(VALID_PARENT_NAME_AMY);
        EditParentCommand editParentCommand = new EditParentCommand(INDEX_FIRST_PERSON, newParentName);

        Person editedPerson = new PersonBuilder(personToEdit).withParentName(VALID_PARENT_NAME_AMY).build();
        String expectedMessage = String.format(EditParentCommand.MESSAGE_EDIT_PARENT_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(editParentCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditParentCommand editParentCommand = new EditParentCommand(outOfBoundIndex,
                new ParentName(VALID_PARENT_NAME_AMY));

        assertCommandFailure(editParentCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        ParentName newParentName = new ParentName(VALID_PARENT_NAME_AMY);
        EditParentCommand editParentCommand = new EditParentCommand(INDEX_FIRST_PERSON, newParentName);

        Person editedPerson = new PersonBuilder(personToEdit).withParentName(VALID_PARENT_NAME_AMY).build();
        String expectedMessage = String.format(EditParentCommand.MESSAGE_EDIT_PARENT_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        assertCommandSuccess(editParentCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures outOfBoundIndex is still within bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        EditParentCommand editParentCommand = new EditParentCommand(outOfBoundIndex,
                new ParentName(VALID_PARENT_NAME_AMY));

        assertCommandFailure(editParentCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        ParentName parentNameAmy = new ParentName(VALID_PARENT_NAME_AMY);
        ParentName parentNameBob = new ParentName(VALID_PARENT_NAME_BOB);
        EditParentCommand editFirstCommand = new EditParentCommand(INDEX_FIRST_PERSON, parentNameAmy);
        EditParentCommand editSecondCommand = new EditParentCommand(INDEX_SECOND_PERSON, parentNameAmy);

        // same object -> returns true
        assertTrue(editFirstCommand.equals(editFirstCommand));

        // same values -> returns true
        EditParentCommand editFirstCommandCopy = new EditParentCommand(INDEX_FIRST_PERSON, parentNameAmy);
        assertTrue(editFirstCommand.equals(editFirstCommandCopy));

        // different types -> returns false
        assertFalse(editFirstCommand.equals(1));

        // null -> returns false
        assertFalse(editFirstCommand.equals(null));

        // different index -> returns false
        assertFalse(editFirstCommand.equals(editSecondCommand));

        // different parent name -> returns false
        EditParentCommand editFirstCommandDiffName = new EditParentCommand(INDEX_FIRST_PERSON, parentNameBob);
        assertFalse(editFirstCommand.equals(editFirstCommandDiffName));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        ParentName parentName = new ParentName(VALID_PARENT_NAME_AMY);
        EditParentCommand editParentCommand = new EditParentCommand(index, parentName);
        String expected = EditParentCommand.class.getCanonicalName() + "{index=" + index
                + ", parentName=" + parentName + "}";
        assertEquals(expected, editParentCommand.toString());
    }
}
