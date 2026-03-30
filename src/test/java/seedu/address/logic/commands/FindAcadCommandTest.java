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
import seedu.address.model.academic.Subject;
import seedu.address.model.academic.SubjectContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindAcadCommand}.
 */
public class FindAcadCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        SubjectContainsKeywordsPredicate firstPredicate =
                new SubjectContainsKeywordsPredicate(Set.of(new Subject("math", null)));
        SubjectContainsKeywordsPredicate secondPredicate =
                new SubjectContainsKeywordsPredicate(Set.of(new Subject("science", null)));

        FindAcadCommand findFirstCommand = new FindAcadCommand(firstPredicate);
        FindAcadCommand findSecondCommand = new FindAcadCommand(secondPredicate);

        // same object -> true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> true
        FindAcadCommand findFirstCommandCopy = new FindAcadCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> false
        assertFalse(findFirstCommand.equals(1));

        // null -> false
        assertFalse(findFirstCommand.equals(null));

        // different predicates -> false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_singleSubject_multiplePersonsFound() {
        Set<Subject> subjects = Set.of(new Subject("math", null));
        SubjectContainsKeywordsPredicate predicate =
                new SubjectContainsKeywordsPredicate(subjects);
        FindAcadCommand command = new FindAcadCommand(predicate);

        expectedModel.updateFilteredPersonList(predicate);

        int expectedSize = expectedModel.getFilteredPersonList().size();
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, expectedSize);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_multipleSubjects_multiplePersonsFound() {
        Set<Subject> subjects = Set.of(
                new Subject("math", null),
                new Subject("science", null));
        SubjectContainsKeywordsPredicate predicate =
                new SubjectContainsKeywordsPredicate(subjects);
        FindAcadCommand command = new FindAcadCommand(predicate);

        expectedModel.updateFilteredPersonList(predicate);

        int expectedSize = expectedModel.getFilteredPersonList().size();
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, expectedSize);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noMatchingSubjects_noPersonFound() {
        Set<Subject> subjects = Set.of(new Subject("nonexistentsubject", null));
        SubjectContainsKeywordsPredicate predicate =
                new SubjectContainsKeywordsPredicate(subjects);
        FindAcadCommand command = new FindAcadCommand(predicate);

        expectedModel.updateFilteredPersonList(predicate);

        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void toStringMethod() {
        SubjectContainsKeywordsPredicate predicate =
                new SubjectContainsKeywordsPredicate(Set.of(new Subject("math", null)));
        FindAcadCommand command = new FindAcadCommand(predicate);

        String expected = FindAcadCommand.class.getCanonicalName()
                + "{predicate=" + predicate + "}";
        assertEquals(expected, command.toString());
    }
}
