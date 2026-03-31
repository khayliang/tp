package seedu.address.model.billing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.address.model.recurrence.Recurrence;

public class BillingTest {

    private static final LocalDate DUE_DATE = LocalDate.of(2026, 3, 30);
    private static final LocalDate ANOTHER_DUE_DATE = LocalDate.of(2026, 4, 30);
    private static final PaymentHistory DEFAULT_PAYMENT_HISTORY =
            new PaymentHistory(LocalDate.of(2026, 5, 30));

    @Test
    public void constructor_validTuitionFee_success() {
        // Test valid tuition fee
        Billing billing = new Billing(Recurrence.MONTHLY, DUE_DATE, 100.0, DEFAULT_PAYMENT_HISTORY);
        assertEquals(100.0, billing.getTuitionFee());
        assertEquals(DUE_DATE, billing.getCurrentDueDate());
        assertEquals(Recurrence.MONTHLY, billing.getRecurrence());
    }

    @Test
    public void constructor_zeroTuitionFee_success() {
        // Test zero tuition fee
        Billing billing = new Billing(Recurrence.MONTHLY, DUE_DATE, 0.0, DEFAULT_PAYMENT_HISTORY);
        assertEquals(0.0, billing.getTuitionFee());
    }

    @Test
    public void constructor_negativeTuitionFee_throwsIllegalArgumentException() {
        // Test negative tuition fee
        assertThrows(IllegalArgumentException.class, () -> new Billing(
                Recurrence.MONTHLY, DUE_DATE, -1.0, DEFAULT_PAYMENT_HISTORY));
    }

    @Test
    public void constructor_nullRecurrence_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Billing(
                null, DUE_DATE, 100.0, DEFAULT_PAYMENT_HISTORY));
    }

    @Test
    public void constructor_nullDueDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Billing(
                Recurrence.MONTHLY, null, 100.0, DEFAULT_PAYMENT_HISTORY));
    }

    @Test
    public void defaultBilling_returnsDefaultBilling() {
        // Test default billing
        Billing defaultBilling = Billing.defaultBilling();
        assertEquals(0.0, defaultBilling.getTuitionFee());
        assertEquals(Recurrence.MONTHLY, defaultBilling.getRecurrence());
    }

    @Test
    public void getNextDueDate_returnsDatePlusPeriod() {
        // For MONTHLY recurrence, next due date should be current due date + 30 days
        Billing billing = new Billing(Recurrence.MONTHLY, DUE_DATE, 100.0, DEFAULT_PAYMENT_HISTORY);
        LocalDate expectedNextDueDate = DUE_DATE.plusMonths(1);
        assertEquals(expectedNextDueDate, billing.getNextDueDate());
    }

    @Test
    public void updatePaymentDueDate_returnsBillingWithNewDate() {
        // Test updating due date
        Billing original = new Billing(Recurrence.MONTHLY, DUE_DATE, 100.0, DEFAULT_PAYMENT_HISTORY);
        Billing updated = original.updatePaymentDueDate(ANOTHER_DUE_DATE);
        assertEquals(DUE_DATE, original.getCurrentDueDate()); // Original unchanged
        assertEquals(ANOTHER_DUE_DATE, updated.getCurrentDueDate()); // Updated changed
        assertEquals(original.getTuitionFee(), updated.getTuitionFee()); // Fee unchanged
    }

    @Test
    public void advanceDueDate_advancesDateByOneRecurrencePeriod() {
        // Test advancing due date
        Billing original = new Billing(Recurrence.MONTHLY, DUE_DATE, 100.0, DEFAULT_PAYMENT_HISTORY);
        Billing advanced = original.advanceDueDate();
        LocalDate expectedNextDate = DUE_DATE.plusMonths(1);
        assertEquals(expectedNextDate, advanced.getCurrentDueDate());
        assertEquals(original.getTuitionFee(), advanced.getTuitionFee()); // Fee unchanged
    }

    @Test
    public void deleteRecordedPayment_latestDate_rollsBackDueDateAndRemovesDate() {
        PaymentHistory history = new PaymentHistory(
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 4, 1));
        Billing original = new Billing(Recurrence.MONTHLY, DUE_DATE, 100.0, history);

        Billing updated = original.deleteRecordedPayment(LocalDate.of(2026, 4, 1));

        assertEquals(DUE_DATE.minusMonths(1), updated.getCurrentDueDate());
        assertFalse(updated.getPaymentHistory().hasPaidOn(LocalDate.of(2026, 4, 1)));
        assertTrue(updated.getPaymentHistory().hasPaidOn(LocalDate.of(2026, 3, 1)));
    }

    @Test
    public void deleteRecordedPayment_nonLatestDate_keepsDueDateAndRemovesDate() {
        PaymentHistory history = new PaymentHistory(
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 4, 1));
        Billing original = new Billing(Recurrence.MONTHLY, DUE_DATE, 100.0, history);

        Billing updated = original.deleteRecordedPayment(LocalDate.of(2026, 3, 1));

        assertEquals(DUE_DATE, updated.getCurrentDueDate());
        assertFalse(updated.getPaymentHistory().hasPaidOn(LocalDate.of(2026, 3, 1)));
        assertTrue(updated.getPaymentHistory().hasPaidOn(LocalDate.of(2026, 4, 1)));
    }

    @Test
    public void deleteRecordedPayment_weeklyLatestDate_rollsBackBySevenDays() {
        PaymentHistory history = new PaymentHistory(
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 3, 8));
        Billing original = new Billing(Recurrence.WEEKLY, DUE_DATE, 100.0, history);

        Billing updated = original.deleteRecordedPayment(LocalDate.of(2026, 3, 8));

        assertEquals(DUE_DATE.minusDays(7), updated.getCurrentDueDate());
        assertFalse(updated.getPaymentHistory().hasPaidOn(LocalDate.of(2026, 3, 8)));
        assertTrue(updated.getPaymentHistory().hasPaidOn(LocalDate.of(2026, 3, 1)));
    }

    @Test
    public void deleteRecordedPayment_noneLatestDate_keepsDueDateUnchanged() {
        PaymentHistory history = new PaymentHistory(LocalDate.of(2026, 3, 1));
        Billing original = new Billing(Recurrence.NONE, DUE_DATE, 100.0, history);

        Billing updated = original.deleteRecordedPayment(LocalDate.of(2026, 3, 1));

        assertEquals(DUE_DATE, updated.getCurrentDueDate());
        assertFalse(updated.getPaymentHistory().hasPaidOn(LocalDate.of(2026, 3, 1)));
    }

    @Test
    public void deleteRecordedPayment_missingDate_throwsIllegalArgumentException() {
        Billing original = new Billing(Recurrence.MONTHLY, DUE_DATE, 100.0,
                new PaymentHistory(LocalDate.of(2026, 4, 1)));
        assertThrows(IllegalArgumentException.class, () ->
                original.deleteRecordedPayment(LocalDate.of(2026, 5, 1)));
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        Billing billing = new Billing(Recurrence.MONTHLY, DUE_DATE, 50.0, DEFAULT_PAYMENT_HISTORY);
        assertTrue(billing.equals(billing));
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        Billing billing1 = new Billing(Recurrence.MONTHLY, DUE_DATE, 50.0, DEFAULT_PAYMENT_HISTORY);
        Billing billing2 = new Billing(Recurrence.MONTHLY, DUE_DATE, 50.0, DEFAULT_PAYMENT_HISTORY);
        assertTrue(billing1.equals(billing2));
    }

    @Test
    public void equals_differentTuitionFee_returnsFalse() {
        Billing billing1 = new Billing(Recurrence.MONTHLY, DUE_DATE, 50.0, DEFAULT_PAYMENT_HISTORY);
        Billing billing2 = new Billing(Recurrence.MONTHLY, DUE_DATE, 60.0, DEFAULT_PAYMENT_HISTORY);
        assertFalse(billing1.equals(billing2));
    }

    @Test
    public void equals_differentDueDate_returnsFalse() {
        Billing billing1 = new Billing(Recurrence.MONTHLY, DUE_DATE, 50.0, DEFAULT_PAYMENT_HISTORY);
        Billing billing2 = new Billing(Recurrence.MONTHLY, ANOTHER_DUE_DATE, 50.0, DEFAULT_PAYMENT_HISTORY);
        assertFalse(billing1.equals(billing2));
    }

    @Test
    public void equals_differentRecurrence_returnsFalse() {
        Billing billing1 = new Billing(Recurrence.MONTHLY, DUE_DATE, 50.0, DEFAULT_PAYMENT_HISTORY);
        Billing billing2 = new Billing(Recurrence.NONE, DUE_DATE, 50.0, DEFAULT_PAYMENT_HISTORY);
        assertFalse(billing1.equals(billing2));
    }

    @Test
    public void equals_null_returnsFalse() {
        Billing billing = new Billing(Recurrence.MONTHLY, DUE_DATE, 50.0, DEFAULT_PAYMENT_HISTORY);
        assertFalse(billing.equals(null));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        Billing billing = new Billing(Recurrence.MONTHLY, DUE_DATE, 50.0, DEFAULT_PAYMENT_HISTORY);
        assertFalse(billing.equals("50.0"));
    }

    @Test
    public void hashCode_sameValues_sameHashCode() {
        Billing billing1 = new Billing(Recurrence.MONTHLY, DUE_DATE, 50.0, DEFAULT_PAYMENT_HISTORY);
        Billing billing2 = new Billing(Recurrence.MONTHLY, DUE_DATE, 50.0, DEFAULT_PAYMENT_HISTORY);
        assertEquals(billing1.hashCode(), billing2.hashCode());
    }

    @Test
    public void hashCode_differentTuitionFee_differentHashCode() {
        Billing billing1 = new Billing(Recurrence.MONTHLY, DUE_DATE, 50.0, DEFAULT_PAYMENT_HISTORY);
        Billing billing2 = new Billing(Recurrence.MONTHLY, DUE_DATE, 60.0, DEFAULT_PAYMENT_HISTORY);
        assertNotEquals(billing1.hashCode(), billing2.hashCode());
    }

    @Test
    public void toString_validBilling_returnsFormattedString() {
        Billing billing = new Billing(Recurrence.MONTHLY, DUE_DATE, 123.45, DEFAULT_PAYMENT_HISTORY);
        String expected = String.format(
            "Billing[recurrence=%s, paymentDueDate=%s, tuitionFee=%.2f, paymentHistory=%s]",
            Recurrence.MONTHLY, DUE_DATE, 123.45, DEFAULT_PAYMENT_HISTORY);
        assertEquals(expected, billing.toString());
    }
}
