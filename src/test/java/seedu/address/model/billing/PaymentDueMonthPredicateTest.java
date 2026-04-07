package seedu.address.model.billing;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.getPersonBuilder;

import java.time.LocalDate;
import java.time.YearMonth;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;
import seedu.address.model.recurrence.Recurrence;

public class PaymentDueMonthPredicateTest {

    @Test
    public void equals() {
        PaymentDueMonthPredicate first = new PaymentDueMonthPredicate(YearMonth.of(2026, 3));
        PaymentDueMonthPredicate second = new PaymentDueMonthPredicate(YearMonth.of(2026, 4));

        // same object
        assertTrue(first.equals(first));

        // same values
        PaymentDueMonthPredicate firstCopy = new PaymentDueMonthPredicate(YearMonth.of(2026, 3));
        assertTrue(first.equals(firstCopy));

        // different types
        assertFalse(first.equals(1));

        // null
        assertFalse(first.equals(null));

        // different value
        assertFalse(first.equals(second));
    }

    @Test
    public void test_personDueDateMatches_returnsTrue() {
        Person person = getPersonBuilder().withBilling(
            new Billing(Recurrence.MONTHLY, LocalDate.of(2026, 3, 5), 0.0, PaymentHistory.EMPTY)).build();

        PaymentDueMonthPredicate predicate = new PaymentDueMonthPredicate(YearMonth.of(2026, 3));
        assertTrue(predicate.test(person));
    }

    @Test
    public void test_personDueDateDoesNotMatch_returnsFalse() {
        Person person = getPersonBuilder().withBilling(
            new Billing(Recurrence.MONTHLY, LocalDate.of(2026, 4, 1), 0.0, PaymentHistory.EMPTY)).build();

        PaymentDueMonthPredicate predicate = new PaymentDueMonthPredicate(YearMonth.of(2026, 3));
        assertFalse(predicate.test(person));
    }
}
