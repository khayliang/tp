package seedu.address.model.billing;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.function.Predicate;

import seedu.address.model.person.Person;

/**
 * Tests whether a {@code Person}'s billing due date falls within a target YearMonth.
 */
public class PaymentDueMonthPredicate implements Predicate<Person> {

    private final YearMonth targetMonth;

    /**
     * Creates a {@code PaymentDueMonthPredicate} object with target month and year
     * @param targetMonth target date to check
     */
    public PaymentDueMonthPredicate(YearMonth targetMonth) {
        requireNonNull(targetMonth);
        this.targetMonth = targetMonth;
    }

    @Override
    public boolean test(Person person) {
        requireNonNull(person);
        LocalDate due = person.getBilling().getLastDueDate();
        if (due == null) {
            return false;
        }
        return YearMonth.from(due).equals(targetMonth);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof PaymentDueMonthPredicate
                && targetMonth.equals(((PaymentDueMonthPredicate) other).targetMonth));
    }
}
