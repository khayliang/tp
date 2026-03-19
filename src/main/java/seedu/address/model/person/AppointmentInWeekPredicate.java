package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Tests whether a person's appointment date falls within the same Monday-Sunday week as the target date.
 */
public class AppointmentInWeekPredicate implements Predicate<Person> {

    private final LocalDate weekStart;
    private final LocalDate weekEnd;

    /**
     * Creates a predicate for the Monday-Sunday week containing {@code targetDate}.
     */
    public AppointmentInWeekPredicate(LocalDate targetDate) {
        requireNonNull(targetDate);
        this.weekStart = targetDate.with(DayOfWeek.MONDAY);
        this.weekEnd = weekStart.plusDays(6);
    }

    public LocalDate getWeekStart() {
        return weekStart;
    }

    public LocalDate getWeekEnd() {
        return weekEnd;
    }

    @Override
    public boolean test(Person person) {
        return person.getAppointmentStart()
                .map(appointmentStart -> appointmentStart.toLocalDate())
                .filter(date -> !date.isBefore(weekStart) && !date.isAfter(weekEnd))
                .isPresent();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AppointmentInWeekPredicate)) {
            return false;
        }
        AppointmentInWeekPredicate otherPredicate = (AppointmentInWeekPredicate) other;
        return weekStart.equals(otherPredicate.weekStart)
                && weekEnd.equals(otherPredicate.weekEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weekStart, weekEnd);
    }
}
