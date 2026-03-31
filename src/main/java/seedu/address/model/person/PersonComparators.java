package seedu.address.model.person;

import java.time.LocalDateTime;
import java.util.Comparator;

/**
 * Common comparators used for person list ordering.
 */
public final class PersonComparators {

    /**
     * Orders persons by next appointment date-time, then by name.
     * Persons without an appointment are ordered last.
     */
    public static final Comparator<Person> APPOINTMENT_ORDER =
            Comparator.comparing((Person person) -> person.getAppointmentNext().orElse(LocalDateTime.MAX))
                    .thenComparing(person -> person.getName().fullName.toLowerCase());

    private PersonComparators() {}
}
