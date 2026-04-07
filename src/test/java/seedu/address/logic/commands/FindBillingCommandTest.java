package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getPersonBuilder;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.billing.Billing;
import seedu.address.model.billing.PaymentDueMonthPredicate;
import seedu.address.model.billing.PaymentHistory;
import seedu.address.model.person.Person;
import seedu.address.model.recurrence.Recurrence;

/**
 * Contains integration tests (interaction with the Model) for {@code FindBillingCommand}.
 */
public class FindBillingCommandTest {

    private Model model = new ModelManager(new AddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(new AddressBook(), new UserPrefs());

    @Test
    public void equals() {
        PaymentDueMonthPredicate firstPredicate =
                new PaymentDueMonthPredicate(YearMonth.of(2020, 1));
        PaymentDueMonthPredicate secondPredicate =
                new PaymentDueMonthPredicate(YearMonth.of(2020, 2));

        FindBillingCommand findFirstCommand = new FindBillingCommand(firstPredicate);
        FindBillingCommand findSecondCommand = new FindBillingCommand(secondPredicate);

        // same object -> true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> true
        FindBillingCommand findFirstCommandCopy = new FindBillingCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> false
        assertFalse(findFirstCommand.equals(1));

        // null -> false
        assertFalse(findFirstCommand.equals(null));

        // different predicates -> false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_singleMonth_multiplePersonsFound() {
        // Person A and B have due date in 2020-01, Person C in 2020-02
        Person personA = getPersonBuilder("Alice Payment").withBilling(
                new Billing(Recurrence.MONTHLY, LocalDate.of(2020, 1, 1), 0.0, PaymentHistory.EMPTY)).build();
        Person personB = getPersonBuilder("Bob Payment").withBilling(
                new Billing(Recurrence.MONTHLY, LocalDate.of(2020, 1, 15), 0.0, PaymentHistory.EMPTY)).build();
        Person personC = getPersonBuilder("Charlie Payment").withBilling(
                new Billing(Recurrence.MONTHLY, LocalDate.of(2020, 2, 1), 0.0, PaymentHistory.EMPTY)).build();

        model.addPerson(personA);
        model.addPerson(personB);
        model.addPerson(personC);

        expectedModel.addPerson(personA);
        expectedModel.addPerson(personB);
        expectedModel.addPerson(personC);

        PaymentDueMonthPredicate predicate = new PaymentDueMonthPredicate(YearMonth.of(2020, 1));
        FindBillingCommand command = new FindBillingCommand(predicate);

        expectedModel.updateFilteredPersonList(predicate);

        int expectedSize = expectedModel.getFilteredPersonList().size();
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, expectedSize);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noMatchingMonth_noPersonFound() {
        Person person = getPersonBuilder("Solo").withBilling(
                new Billing(Recurrence.MONTHLY, LocalDate.of(2021, 3, 1), 0.0, PaymentHistory.EMPTY)).build();

        model.addPerson(person);
        expectedModel.addPerson(person);

        PaymentDueMonthPredicate predicate = new PaymentDueMonthPredicate(YearMonth.of(1999, 12));
        FindBillingCommand command = new FindBillingCommand(predicate);

        expectedModel.updateFilteredPersonList(predicate);

        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 0);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void toStringMethod() {
        PaymentDueMonthPredicate predicate = new PaymentDueMonthPredicate(YearMonth.of(2020, 1));
        FindBillingCommand command = new FindBillingCommand(predicate);

        String expected = FindBillingCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, command.toString());
    }
}
