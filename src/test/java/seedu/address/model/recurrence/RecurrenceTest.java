package seedu.address.model.recurrence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class RecurrenceTest {

    @Test
    public void getDays_monthly_returns30() {
        assertEquals(30, Recurrence.MONTHLY.getDays());
    }

    @Test
    public void getDays_none_returns0() {
        assertEquals(0, Recurrence.NONE.getDays());
    }
}
