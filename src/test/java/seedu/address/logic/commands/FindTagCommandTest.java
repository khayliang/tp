package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindTagCommand}.
 */
public class FindTagCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        TagContainsKeywordsPredicate firstPredicate =
                new TagContainsKeywordsPredicate(Set.of(new Tag("math")));
        TagContainsKeywordsPredicate secondPredicate =
                new TagContainsKeywordsPredicate(Set.of(new Tag("science")));

        FindTagCommand findFirstCommand = new FindTagCommand(firstPredicate);
        FindTagCommand findSecondCommand = new FindTagCommand(secondPredicate);

        // same object -> true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> true
        FindTagCommand findFirstCommandCopy = new FindTagCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> false
        assertFalse(findFirstCommand.equals(1));

        // null -> false
        assertFalse(findFirstCommand.equals(null));

        // different predicates -> false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_singleTag_multiplePersonsFound() {
        Set<Tag> tags = Set.of(new Tag("friends"));
        TagContainsKeywordsPredicate predicate = new TagContainsKeywordsPredicate(tags);
        FindTagCommand command = new FindTagCommand(predicate);

        expectedModel.updateFilteredPersonList(predicate);

        int expectedSize = expectedModel.getFilteredPersonList().size();
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, expectedSize);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_multipleTags_multiplePersonsFound() {
        Set<Tag> tags = Set.of(new Tag("friends"), new Tag("school"));
        TagContainsKeywordsPredicate predicate = new TagContainsKeywordsPredicate(tags);
        FindTagCommand command = new FindTagCommand(predicate);

        expectedModel.updateFilteredPersonList(predicate);

        int expectedSize = expectedModel.getFilteredPersonList().size();
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, expectedSize);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noMatchingTags_noPersonFound() {
        Set<Tag> tags = Set.of(new Tag("nonexistenttag"));
        TagContainsKeywordsPredicate predicate = new TagContainsKeywordsPredicate(tags);
        FindTagCommand command = new FindTagCommand(predicate);

        expectedModel.updateFilteredPersonList(predicate);

        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void toStringMethod() {
        TagContainsKeywordsPredicate predicate =
                new TagContainsKeywordsPredicate(Set.of(new Tag("math")));
        FindTagCommand command = new FindTagCommand(predicate);

        String expected = FindTagCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, command.toString());
    }
}
