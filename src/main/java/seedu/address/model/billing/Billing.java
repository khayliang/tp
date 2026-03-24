package seedu.address.model.billing;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;

import seedu.address.model.recurrence.Recurrence;

/**
 * Represents billing configuration for a tutee.
 * Guarantees: immutable.
 */
public class Billing {

    private static final double DEFAULT_TUITION_FEE = 0.0;
    private static final LocalDate DEFAULT_PAYMENT_DUE_DATE = LocalDate.now().withDayOfMonth(1);

    private final Recurrence recurrence;
    private final LocalDate paymentDueDate;
    private final double tuitionFee;
    private final PaymentHistory paymentHistory;

    /**
     * Creates a billing record with a tuition fee
     * @param tuitionFee A non-negative amount
     */
    public Billing(Recurrence recurrence, LocalDate paymentDueDate, Double tuitionfee, PaymentHistory paymentHistory) {
        requireNonNull(recurrence);
        requireNonNull(paymentDueDate);
        requireNonNull(paymentHistory);
        checkArgument(tuitionfee >= 0, "Tuition fee must be non-negative");
        this.recurrence = recurrence;
        this.paymentDueDate = paymentDueDate;
        this.tuitionFee = tuitionfee;
        this.paymentHistory = paymentHistory;
    }

    /**
     * Returns a new {@code Billing} object with default monthly rate
     * @return {@code Billing} object with default monthly rate
     */
    public static Billing defaultBilling() {
        return new Billing(
                Recurrence.MONTHLY,
                DEFAULT_PAYMENT_DUE_DATE,
                DEFAULT_TUITION_FEE,
                PaymentHistory.EMPTY);
    }

    public Recurrence getRecurrence() {
        return recurrence;
    }

    public LocalDate getLastDueDate() {
        return paymentDueDate;
    }

    public Double getTuitionFee() {
        return tuitionFee;
    }

    public PaymentHistory getPaymentHistory() {
        return paymentHistory;
    }

    /**
     * Returns a new {@code Billing} object with updated tuition fees
     * @return {@code Billing} object with updated tuition fees
     */
    public Billing updateRate(Double newTuitionFees) {
        checkArgument(newTuitionFees >= 0, "Tuition fees must be non-negative");
        return new Billing(
                getRecurrence(), getLastDueDate(), newTuitionFees, getPaymentHistory());
    }

    public LocalDate getNextDueDate() {
        if (recurrence == Recurrence.MONTHLY) {
            return paymentDueDate.plusMonths(1);
        }
        return paymentDueDate.plusDays(recurrence.getDays());
    }

    /**
     * Updates payment due date to the target due date.
     * @param newDueDate Target due date
     * @return new {@code Billing} with updated due date
     */
    public Billing updatePaymentDueDate(LocalDate newDueDate) {
        requireNonNull(newDueDate);
        return new Billing(recurrence, newDueDate, tuitionFee, paymentHistory);
    }

    /**
     * Advances the payment due date by one recurrence cycle.
     * Called when payment is recorded to move to next billing period.
     * @return new {@code Billing} with advanced due date
     */
    public Billing advanceDueDate() {
        return new Billing(recurrence, getNextDueDate(), tuitionFee, paymentHistory);
    }

    /**
     * Records the date the student pays his/her tuition fees
     * @param paymentDate a valid {@code LocalDate}
     * @return new {@code Billing} with updated payment history
     */
    public Billing recordTuitionPaid(LocalDate paymentDate) {
        PaymentHistory updatedPaymentHistory = paymentHistory.recordPayment(paymentDate);
        return new Billing(recurrence, paymentDueDate, tuitionFee, updatedPaymentHistory);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Billing)) {
            return false;
        }
        Billing otherBilling = (Billing) other;
        return recurrence.equals(otherBilling.recurrence)
                && paymentDueDate.equals(otherBilling.paymentDueDate)
                && Double.compare(tuitionFee, otherBilling.tuitionFee) == 0
                && paymentHistory.equals(otherBilling.paymentHistory);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(recurrence, paymentDueDate, tuitionFee, paymentHistory);
    }

    @Override
    public String toString() {
        return String.format(
                "Billing[recurrence=%s, paymentDueDate=%s, tuitionFee=%.2f, paymentHistory=%s]",
                recurrence, paymentDueDate, tuitionFee, paymentHistory);
    }
}

