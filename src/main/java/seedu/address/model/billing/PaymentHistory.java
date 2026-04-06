package seedu.address.model.billing;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Represents payment record for a tutee.
 * Guarantees: immutable.
 */
public class PaymentHistory {

    public static final PaymentHistory EMPTY = new PaymentHistory();

    private final Set<LocalDate> paidDates;

    /**
     * Creates a {@code PaymentHistory} object with payment history
     * @param paidDates Payment history
     */
    public PaymentHistory(LocalDate... paidDates) {
        requireNonNull(paidDates);
        this.paidDates = Collections.unmodifiableSet(new LinkedHashSet<>(Arrays.asList(paidDates)));
    }

    public Set<LocalDate> getPaidDates() {
        return paidDates;
    }

    /**
     * Record payment made on {@code date}
     * @param date A valid date
     * @return {@code PaymentHistory} object with updated payment history
     * @throws IllegalArgumentException if {@code date} is already in payment history
     */
    public PaymentHistory recordPayment(LocalDate date) {
        requireNonNull(date);
        checkArgument(!paidDates.contains(date), "Payment date is already present");

        Set<LocalDate> next = new LinkedHashSet<>(paidDates);
        next.add(date);
        return new PaymentHistory(next.toArray(LocalDate[]::new));
    }

    /**
     * Deletes payment made on {@code date}
     * @param date A valid date
     * @return {@code PaymentHistory} object with updated payment history
     * @throws IllegalArgumentException if {@code date} is not in payment history
     */
    public PaymentHistory removePayment(LocalDate date) {
        requireNonNull(date);
        checkArgument(hasPaidOn(date), "Payment date not found");
        Set<LocalDate> next = new LinkedHashSet<>(paidDates);
        next.remove(date);
        return new PaymentHistory(next.toArray(LocalDate[]::new));
    }

    /**
     * Returns latest date in payment history if present
     */
    public Optional<LocalDate> getLatestPaidDate() {
        return paidDates.stream().max(LocalDate::compareTo);
    }

    /**
     * Checks if given {@code date} is in recorded payment history
     * @param date A valid date
     * @return {@code True} if date is present in payment history,
     *      {@code False} otherwise
     */
    public boolean hasPaidOn(LocalDate date) {
        requireNonNull(date);
        return paidDates.contains(date);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof PaymentHistory)) {
            return false;
        }
        PaymentHistory otherPayment = (PaymentHistory) other;
        return paidDates.equals(otherPayment.paidDates);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(paidDates);
    }

    @Override
    public String toString() {
        return String.format("Payment[paidDates=%s]", paidDates);
    }
}
