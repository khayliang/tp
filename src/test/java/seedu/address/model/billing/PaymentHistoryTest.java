package seedu.address.model.billing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class PaymentHistoryTest {

    private static final LocalDate DATE_1 = LocalDate.of(2026, 1, 1);
    private static final LocalDate DATE_2 = LocalDate.of(2026, 2, 1);
    private static final LocalDate DATE_3 = LocalDate.of(2026, 3, 1);

    @Test
    public void constructor_validPaidDates_success() {
        PaymentHistory payment = new PaymentHistory(DATE_1);
        assertEquals(1, payment.getPaidDates().size());
        assertTrue(payment.getPaidDates().contains(DATE_1));
    }

    @Test
    public void constructor_emptyPaidDates_success() {
        PaymentHistory payment = new PaymentHistory();
        assertEquals(0, payment.getPaidDates().size());
    }

    @Test
    public void withInitialDate_validDate_returnsPaymentWithDate() {
        PaymentHistory payment = new PaymentHistory(DATE_1);
        assertTrue(payment.getPaidDates().contains(DATE_1));
        assertEquals(1, payment.getPaidDates().size());
    }

    @Test
    public void recordPayment_validDate_returnsNewPaymentWithAddedDate() {
        PaymentHistory original = PaymentHistory.EMPTY;
        PaymentHistory updated = original.recordPayment(DATE_1);
        assertTrue(updated.getPaidDates().contains(DATE_1));
        assertFalse(original.getPaidDates().contains(DATE_1)); // Original unchanged (immutable)
    }

    @Test
    public void recordPayment_addMultipleDates_returnsPaymentWithAllDates() {
        PaymentHistory payment = PaymentHistory.EMPTY;
        payment = payment.recordPayment(DATE_1);
        payment = payment.recordPayment(DATE_2);
        payment = payment.recordPayment(DATE_3);
        assertEquals(3, payment.getPaidDates().size());
        assertTrue(payment.getPaidDates().contains(DATE_1));
        assertTrue(payment.getPaidDates().contains(DATE_2));
        assertTrue(payment.getPaidDates().contains(DATE_3));
    }

    @Test
    public void recordPayment_nullDate_throwsNullPointerException() {
        PaymentHistory payment = PaymentHistory.EMPTY;
        assertThrows(NullPointerException.class, () -> payment.recordPayment(null));
    }

    @Test
    public void recordPayment_duplicateDate_ignoresDuplicate() {
        PaymentHistory payment = new PaymentHistory(DATE_1);
        PaymentHistory updated = payment.recordPayment(DATE_1);
        assertEquals(1, updated.getPaidDates().size());
    }

    @Test
    public void removePayment_existingDate_returnsNewPaymentWithoutDate() {
        PaymentHistory original = new PaymentHistory(DATE_1, DATE_2);
        PaymentHistory updated = original.removePayment(DATE_2);
        assertTrue(original.getPaidDates().contains(DATE_2));
        assertFalse(updated.getPaidDates().contains(DATE_2));
        assertEquals(1, updated.getPaidDates().size());
    }

    @Test
    public void removePayment_missingDate_throwsIllegalArgumentException() {
        PaymentHistory payment = new PaymentHistory(DATE_1);
        assertThrows(IllegalArgumentException.class, () -> payment.removePayment(DATE_2));
    }

    @Test
    public void getLatestPaidDate_nonEmptyHistory_returnsLatest() {
        PaymentHistory payment = new PaymentHistory(DATE_1, DATE_3, DATE_2);
        assertEquals(DATE_3, payment.getLatestPaidDate().get());
    }

    @Test
    public void getLatestPaidDate_emptyHistory_returnsEmpty() {
        PaymentHistory payment = PaymentHistory.EMPTY;
        assertTrue(payment.getLatestPaidDate().isEmpty());
    }

    @Test
    public void hasPaidOn_dateInHistory_returnsTrue() {
        PaymentHistory payment = new PaymentHistory(DATE_1);
        assertTrue(payment.hasPaidOn(DATE_1));
    }

    @Test
    public void hasPaidOn_dateNotInHistory_returnsFalse() {
        PaymentHistory payment = new PaymentHistory(DATE_1);
        assertFalse(payment.hasPaidOn(DATE_2));
    }

    @Test
    public void hasPaidOn_nullDate_throwsNullPointerException() {
        PaymentHistory payment = PaymentHistory.EMPTY;
        assertThrows(NullPointerException.class, () -> payment.hasPaidOn(null));
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        PaymentHistory payment = PaymentHistory.EMPTY;
        assertTrue(payment.equals(payment));
    }

    @Test
    public void equals_emptyPayments_returnsTrue() {
        PaymentHistory payment1 = PaymentHistory.EMPTY;
        PaymentHistory payment2 = PaymentHistory.EMPTY;
        assertTrue(payment1.equals(payment2));
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        PaymentHistory payment1 = new PaymentHistory(DATE_1, DATE_2);
        PaymentHistory payment2 = new PaymentHistory(DATE_1, DATE_2);
        assertTrue(payment1.equals(payment2));
    }

    @Test
    public void equals_differentDates_returnsFalse() {
        PaymentHistory payment1 = new PaymentHistory(DATE_1);
        PaymentHistory payment2 = new PaymentHistory(DATE_2);
        assertFalse(payment1.equals(payment2));
    }

    @Test
    public void equals_differentDateCount_returnsFalse() {
        PaymentHistory payment1 = new PaymentHistory(DATE_1);
        PaymentHistory payment2 = new PaymentHistory(DATE_1, DATE_2);
        assertFalse(payment1.equals(payment2));
    }

    @Test
    public void equals_null_returnsFalse() {
        PaymentHistory payment = PaymentHistory.EMPTY;
        assertFalse(payment.equals(null));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        PaymentHistory payment = PaymentHistory.EMPTY;
        assertFalse(payment.equals("payment"));
    }

    @Test
    public void hashCode_sameValues_sameHashCode() {
        PaymentHistory payment1 = new PaymentHistory(DATE_1);
        PaymentHistory payment2 = new PaymentHistory(DATE_1);
        assertEquals(payment1.hashCode(), payment2.hashCode());
    }

    @Test
    public void hashCode_differentDates_differentHashCode() {
        PaymentHistory payment1 = new PaymentHistory(DATE_1);
        PaymentHistory payment2 = new PaymentHistory(DATE_2);
        assertNotEquals(payment1.hashCode(), payment2.hashCode());
    }

    @Test
    public void toString_validPayment_returnsFormattedString() {
        PaymentHistory payment = new PaymentHistory(DATE_1);
        String result = payment.toString();
        assertTrue(result.contains("Payment"));
        assertTrue(result.contains(DATE_1.toString()));
    }

    @Test
    public void getPaidDates_returnsUnmodifiableSet() {
        PaymentHistory payment = new PaymentHistory(DATE_1);
        Set<LocalDate> retrievedDates = payment.getPaidDates();
        assertThrows(UnsupportedOperationException.class, () ->
                retrievedDates.add(DATE_2)); // Should not be able to modify
    }
}
