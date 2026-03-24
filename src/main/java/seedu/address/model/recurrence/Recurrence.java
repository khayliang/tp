package seedu.address.model.recurrence;

/**
 * Recurrence schedule options for recurring dates.
 */
public enum Recurrence {
    WEEKLY(7),
    BIWEEKLY(14),
    MONTHLY(30),
    NONE(0);

    private final int days;

    /**
     * Creates a {@code Recurrence} object with specified days
     *      to next schedule
     * @param days A valid integer
     */
    Recurrence(int days) {
        this.days = days;
    }

    public int getDays() {
        return days;
    }
}
