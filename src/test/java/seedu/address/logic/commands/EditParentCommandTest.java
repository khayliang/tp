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
import seedu.address.logic.commands.EditParentCommand.EditParentDescriptor;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Email;
import seedu.address.model.person.Guardian;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;
import seedu.address.model.person.Phone;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code EditParentCommand}.
 */
public class EditParentCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Name newParentName = new Name(VALID_PARENT_NAME_AMY);
        EditParentDescriptor descriptor = new EditParentDescriptor();
        descriptor.setParentName(newParentName);
        EditParentCommand editParentCommand = new EditParentCommand(INDEX_FIRST_PERSON, descriptor);

        Person editedPerson = new PersonBuilder(personToEdit)
                .withGuardian(new Guardian(new Name(VALID_PARENT_NAME_AMY), null, null))
                .build();
        String expectedMessage = String.format(EditParentCommand.MESSAGE_EDIT_PARENT_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(editParentCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_editPersonWithExistingGuardian_success() {
        // BENSON (index 2) already has a guardian with parentName "Susan Meier"
        Person personToEdit = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());

        // Edit only phone — existing name should be preserved
        Phone newPhone = new Phone("99998888");
        EditParentDescriptor descriptor = new EditParentDescriptor();
        descriptor.setParentPhone(newPhone);
        EditParentCommand editParentCommand = new EditParentCommand(INDEX_SECOND_PERSON, descriptor);

        Person editedPerson = new PersonBuilder(personToEdit)
                .withGuardian(new Guardian(
                        personToEdit.getGuardian().orElseThrow().getName(),
                        new Phone("99998888"),
                        personToEdit.getGuardian().orElseThrow().getEmail().orElse(null)))
                .build();
        String expectedMessage = String.format(EditParentCommand.MESSAGE_EDIT_PARENT_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(editParentCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_editAllParentFields_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Name newName = new Name(VALID_PARENT_NAME_AMY);
        Phone newPhone = new Phone("91234567");
        Email newEmail = new Email("parent@example.com");
        EditParentDescriptor descriptor = new EditParentDescriptor();
        descriptor.setParentName(newName);
        descriptor.setParentPhone(newPhone);
        descriptor.setParentEmail(newEmail);
        EditParentCommand editParentCommand = new EditParentCommand(INDEX_FIRST_PERSON, descriptor);

        Person editedPerson = new PersonBuilder(personToEdit)
                .withGuardian(new Guardian(
                        new Name(VALID_PARENT_NAME_AMY), new Phone("91234567"), new Email("parent@example.com")))
                .build();
        String expectedMessage = String.format(EditParentCommand.MESSAGE_EDIT_PARENT_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(editParentCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditParentDescriptor descriptor = new EditParentDescriptor();
        descriptor.setParentName(new Name(VALID_PARENT_NAME_AMY));
        EditParentCommand editParentCommand = new EditParentCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editParentCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Name newParentName = new Name(VALID_PARENT_NAME_AMY);
        EditParentDescriptor descriptor = new EditParentDescriptor();
        descriptor.setParentName(newParentName);
        EditParentCommand editParentCommand = new EditParentCommand(INDEX_FIRST_PERSON, descriptor);

        Person editedPerson = new PersonBuilder(personToEdit)
                .withGuardian(new Guardian(new Name(VALID_PARENT_NAME_AMY), null, null))
                .build();
        String expectedMessage = String.format(EditParentCommand.MESSAGE_EDIT_PARENT_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);
        expectedModel.setPerson(personToEdit, editedPerson);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(editParentCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures outOfBoundIndex is still within bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        EditParentDescriptor descriptor = new EditParentDescriptor();
        descriptor.setParentName(new Name(VALID_PARENT_NAME_AMY));
        EditParentCommand editParentCommand = new EditParentCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editParentCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        Name parentNameAmy = new Name(VALID_PARENT_NAME_AMY);
        Name parentNameBob = new Name(VALID_PARENT_NAME_BOB);

        EditParentDescriptor descriptorAmy = new EditParentDescriptor();
        descriptorAmy.setParentName(parentNameAmy);

        EditParentCommand editFirstCommand = new EditParentCommand(INDEX_FIRST_PERSON, descriptorAmy);
        EditParentCommand editSecondCommand = new EditParentCommand(INDEX_SECOND_PERSON, descriptorAmy);

        // same object -> returns true
        assertTrue(editFirstCommand.equals(editFirstCommand));

        // same values -> returns true
        EditParentDescriptor descriptorAmyCopy = new EditParentDescriptor();
        descriptorAmyCopy.setParentName(parentNameAmy);
        EditParentCommand editFirstCommandCopy = new EditParentCommand(INDEX_FIRST_PERSON, descriptorAmyCopy);
        assertTrue(editFirstCommand.equals(editFirstCommandCopy));

        // different types -> returns false
        assertFalse(editFirstCommand.equals(1));

        // null -> returns false
        assertFalse(editFirstCommand.equals(null));

        // different index -> returns false
        assertFalse(editFirstCommand.equals(editSecondCommand));

        // different parent name -> returns false
        EditParentDescriptor descriptorBob = new EditParentDescriptor();
        descriptorBob.setParentName(parentNameBob);
        EditParentCommand editFirstCommandDiffName = new EditParentCommand(INDEX_FIRST_PERSON, descriptorBob);
        assertFalse(editFirstCommand.equals(editFirstCommandDiffName));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        Name parentName = new Name(VALID_PARENT_NAME_AMY);
        EditParentDescriptor descriptor = new EditParentDescriptor();
        descriptor.setParentName(parentName);
        EditParentCommand editParentCommand = new EditParentCommand(index, descriptor);
        String expected = EditParentCommand.class.getCanonicalName() + "{index=" + index + ", editParentDescriptor="
                + descriptor + "}";
        assertEquals(expected, editParentCommand.toString());
    }
}
