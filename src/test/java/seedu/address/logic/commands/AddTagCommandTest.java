package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.ListDisplayMode;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;
import seedu.address.model.tag.Tag;

/**
 * Contains integration tests and unit tests for AddTagCommand.
 */
public class AddTagCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("newtag"));

        AddTagCommand addCommand = new AddTagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        Set<Tag> expectedTags = new HashSet<>(personToEdit.getTags());
        expectedTags.addAll(tagsToAdd);

        Person editedPerson = new PersonBuilder(personToEdit)
                .withTags(expectedTags)
                .build();

        String expectedMessage = String.format(AddTagCommand.MESSAGE_ADD_TAG_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(addCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_duplicateTag_noChangeButSuccess() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Tag existingTag = personToEdit.getTags().iterator().next();
        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(existingTag);

        AddTagCommand addCommand = new AddTagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        Person editedPerson = new PersonBuilder(personToEdit)
                .withTags(personToEdit.getTags()) // no change
                .build();

        String expectedMessage = String.format(AddTagCommand.MESSAGE_ADD_TAG_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(addCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_multipleTags_success() {
        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("tag1"));
        tagsToAdd.add(new Tag("tag2"));

        AddTagCommand addCommand = new AddTagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        Set<Tag> expectedTags = new HashSet<>(personToEdit.getTags());
        expectedTags.addAll(tagsToAdd);

        Person editedPerson = new PersonBuilder(personToEdit)
                .withTags(expectedTags)
                .build();

        String expectedMessage = String.format(AddTagCommand.MESSAGE_ADD_TAG_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToEdit, editedPerson);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(addCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("tag"));

        AddTagCommand addCommand = new AddTagCommand(outOfBoundIndex, tagsToAdd);

        assertCommandFailure(addCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_SECOND_PERSON);

        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("filteredTag"));

        AddTagCommand addCommand = new AddTagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        Set<Tag> expectedTags = new HashSet<>(personToEdit.getTags());
        expectedTags.addAll(tagsToAdd);

        Person editedPerson = new PersonBuilder(personToEdit)
                .withTags(expectedTags)
                .build();

        String expectedMessage = String.format(AddTagCommand.MESSAGE_ADD_TAG_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        showPersonAtIndex(expectedModel, INDEX_SECOND_PERSON);

        Person expectedPersonToEdit =
                expectedModel.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        expectedModel.setPerson(expectedPersonToEdit, editedPerson);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(addCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_validIndexDifferentDisplayMode_success() {
        model.setListDisplayMode(ListDisplayMode.APPOINTMENT);

        Person personToEdit = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Set<Tag> tagsToAdd = new HashSet<>();
        tagsToAdd.add(new Tag("apptTag"));

        AddTagCommand addCommand = new AddTagCommand(INDEX_FIRST_PERSON, tagsToAdd);

        Set<Tag> expectedTags = new HashSet<>(personToEdit.getTags());
        expectedTags.addAll(tagsToAdd);

        Person editedPerson = new PersonBuilder(personToEdit)
                .withTags(expectedTags)
                .build();

        String expectedMessage = String.format(AddTagCommand.MESSAGE_ADD_TAG_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setListDisplayMode(ListDisplayMode.APPOINTMENT);
        expectedModel.setPerson(personToEdit, editedPerson);

        CommandResult expectedCommandResult = new CommandResult(expectedMessage, editedPerson);
        assertCommandSuccess(addCommand, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void equals() {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("tag"));

        AddTagCommand standardCommand = new AddTagCommand(INDEX_FIRST_PERSON, tags);

        AddTagCommand sameCommand = new AddTagCommand(INDEX_FIRST_PERSON, tags);
        AddTagCommand differentIndex = new AddTagCommand(INDEX_SECOND_PERSON, tags);

        Set<Tag> differentTags = new HashSet<>();
        differentTags.add(new Tag("different"));
        AddTagCommand differentTagsCommand = new AddTagCommand(INDEX_FIRST_PERSON, differentTags);

        assertTrue(standardCommand.equals(sameCommand));
        assertTrue(standardCommand.equals(standardCommand));
        assertFalse(standardCommand.equals(null));
        assertFalse(standardCommand.equals(new ClearCommand()));
        assertFalse(standardCommand.equals(differentIndex));
        assertFalse(standardCommand.equals(differentTagsCommand));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);

        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("tag"));

        AddTagCommand command = new AddTagCommand(index, tags);

        String expected = AddTagCommand.class.getCanonicalName()
                + "{index=" + index + ", tagsToAdd=" + tags + "}";

        assertEquals(expected, command.toString());
    }
}
