package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_GROUP1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_JC;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditTagCommand.
 */
public class EditTagCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_replaceTagsUnfilteredList_success() {
        Person personInList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Set<Tag> tags = Set.of(new Tag(VALID_TAG_JC), new Tag(VALID_TAG_GROUP1));

        EditTagCommand editCommand = new EditTagCommand(INDEX_FIRST_PERSON, tags);

        Person editedPerson = new PersonBuilder(personInList)
                .withTags(VALID_TAG_JC, VALID_TAG_GROUP1)
                .build();

        String expectedMessage = String.format(EditTagCommand.MESSAGE_EDIT_TAG_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personInList, editedPerson);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(editCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_clearTags_success() {
        Person personInList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Set<Tag> tags = Set.of();

        EditTagCommand editCommand = new EditTagCommand(INDEX_FIRST_PERSON, tags);

        Person editedPerson = new PersonBuilder(personInList)
                .withTags()
                .build();

        String expectedMessage = String.format(EditTagCommand.MESSAGE_EDIT_TAG_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personInList, editedPerson);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(editCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Set<Tag> tags = Set.of(new Tag(VALID_TAG_JC));

        EditTagCommand editCommand = new EditTagCommand(INDEX_FIRST_PERSON, tags);

        Person editedPerson = new PersonBuilder(personInFilteredList)
                .withTags(VALID_TAG_JC)
                .build();

        String expectedMessage = String.format(EditTagCommand.MESSAGE_EDIT_TAG_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);
        expectedModel.setPerson(personInFilteredList, editedPerson);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(editCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        Set<Tag> tags = Set.of(new Tag(VALID_TAG_JC));

        EditTagCommand editCommand = new EditTagCommand(outOfBoundIndex, tags);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        Set<Tag> tags = Set.of(new Tag(VALID_TAG_JC));

        final EditTagCommand standardCommand = new EditTagCommand(INDEX_FIRST_PERSON, tags);

        // same values -> returns true
        Set<Tag> copyTags = Set.of(new Tag(VALID_TAG_JC));
        EditTagCommand commandWithSameValues = new EditTagCommand(INDEX_FIRST_PERSON, copyTags);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditTagCommand(INDEX_SECOND_PERSON, tags)));

        // different tags -> returns false
        Set<Tag> differentTags = Set.of(new Tag("DIFFERENT"));
        assertFalse(standardCommand.equals(new EditTagCommand(INDEX_FIRST_PERSON, differentTags)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        Set<Tag> tags = Set.of(new Tag(VALID_TAG_JC));

        EditTagCommand command = new EditTagCommand(index, tags);

        String expected = EditTagCommand.class.getCanonicalName()
                + "{index=" + index + ", tags=" + tags + "}";

        assertEquals(expected, command.toString());
    }
}
