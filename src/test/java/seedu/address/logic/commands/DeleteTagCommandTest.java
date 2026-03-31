package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;
import seedu.address.model.tag.Tag;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteTagCommand}.
 */
public class DeleteTagCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validSingleTag_success() {
        Person person = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        List<Tag> sortedTags = person.getSortedTags();

        // assume at least one tag exists in typical data
        Index tagIndex = Index.fromOneBased(1);

        DeleteTagCommand command = new DeleteTagCommand(INDEX_FIRST_PERSON, List.of(tagIndex));

        Tag tagToDelete = sortedTags.get(tagIndex.getZeroBased());

        String expectedMessage = String.format(
                DeleteTagCommand.MESSAGE_DELETE_TAG_SUCCESS,
                tagToDelete.tagName);

        // expected model
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Person expectedPerson = new PersonBuilder(person)
                .withTags(person.getTags().stream()
                        .filter(tag -> !tag.equals(tagToDelete))
                        .collect(java.util.stream.Collectors.toSet()))
                .build();

        expectedModel.setPerson(person, expectedPerson);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidTagIndex_throwsCommandException() {
        Person person = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        int tagSize = person.getSortedTags().size();

        Index invalidTagIndex = Index.fromOneBased(tagSize + 1);

        DeleteTagCommand command = new DeleteTagCommand(
                INDEX_FIRST_PERSON,
                List.of(invalidTagIndex));

        assertCommandFailure(command, model, "Invalid tag index: " + invalidTagIndex.getOneBased());
    }

    @Test
    public void execute_multipleTags_success() {
        Person person = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        List<Tag> sortedTags = person.getSortedTags();

        // ensure at least 2 tags exist
        if (sortedTags.size() < 2) {
            return; // skip test safely
        }

        Index tagIndex1 = Index.fromOneBased(1);
        Index tagIndex2 = Index.fromOneBased(2);

        DeleteTagCommand command = new DeleteTagCommand(
                INDEX_FIRST_PERSON,
                List.of(tagIndex1, tagIndex2));

        Tag tag1 = sortedTags.get(0);
        Tag tag2 = sortedTags.get(1);

        String expectedMessage = String.format(
                DeleteTagCommand.MESSAGE_DELETE_TAG_SUCCESS,
                tag1.tagName + ", " + tag2.tagName);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Person expectedPerson = new PersonBuilder(person)
                .withTags(person.getTags().stream()
                        .filter(tag -> !tag.equals(tag1) && !tag.equals(tag2))
                        .collect(java.util.stream.Collectors.toSet()))
                .build();

        expectedModel.setPerson(person, expectedPerson);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        List<Index> first = List.of(Index.fromOneBased(1));
        List<Index> second = List.of(Index.fromOneBased(2));

        DeleteTagCommand deleteFirst = new DeleteTagCommand(INDEX_FIRST_PERSON, first);
        DeleteTagCommand deleteSecond = new DeleteTagCommand(INDEX_FIRST_PERSON, second);

        // same object
        assertTrue(deleteFirst.equals(deleteFirst));

        // same values
        DeleteTagCommand copy = new DeleteTagCommand(INDEX_FIRST_PERSON, first);
        assertTrue(deleteFirst.equals(copy));

        // different types
        assertFalse(deleteFirst.equals(1));

        // null
        assertFalse(deleteFirst.equals(null));

        // different indices
        assertFalse(deleteFirst.equals(deleteSecond));
    }

    @Test
    public void toStringMethod() {
        List<Index> indices = List.of(Index.fromOneBased(1));
        DeleteTagCommand command = new DeleteTagCommand(INDEX_FIRST_PERSON, indices);

        String expected = DeleteTagCommand.class.getCanonicalName()
                + "{index=" + INDEX_FIRST_PERSON + ", tagIndices=" + indices + "}";

        assertEquals(expected, command.toString());
    }
}
