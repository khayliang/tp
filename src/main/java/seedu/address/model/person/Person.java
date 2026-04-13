package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.academic.Academics;
import seedu.address.model.billing.Billing;
import seedu.address.model.session.Appointment;
import seedu.address.model.session.ScheduledSession;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;
    private final Address address;

    // Data fields
    private final Set<Tag> tags = new HashSet<>();
    private final Academics academics;
    private final Appointment appointment;
    private final Optional<Guardian> guardian;
    private final Billing billing;


    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address,
                  Set<Tag> tags, Academics academics,
                  Optional<Guardian> guardian,
                                    Appointment appointment,
                  Billing billing) {

        requireAllNonNull(name, phone, email, address, tags, academics,
                                guardian, appointment, billing);

        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);

        this.academics = academics;

        this.guardian = guardian;
        this.appointment = appointment;
        this.billing = billing;
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    /**
     * Returns the next upcoming appointment for this person.
     * If all sessions are in the past, returns the most recent past session instead.
     */
    public Optional<ScheduledSession> getNextAppointment() {
        return appointment.getNextSession();
    }

    public Appointment getAppointment() {
        return appointment;
    }



    public Billing getBilling() {
        return billing;
    }



    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public List<Tag> getSortedTags() {
        return tags.stream()
                .sorted(Comparator.comparing(tag -> tag.tagName.toLowerCase()))
                .toList();
    }

    public Academics getAcademics() {
        return academics;
    }

    /**
     * Returns the guardian wrapped in an Optional, or empty if not set.
     */
    public Optional<Guardian> getGuardian() {
        return guardian;
    }

    /**
     * Returns true if both persons have the same name and email.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName())
                && otherPerson.getEmail().equals(getEmail());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && tags.equals(otherPerson.tags)
                && academics.equals(otherPerson.academics)
                && guardian.equals(otherPerson.guardian)
                && appointment.equals(otherPerson.appointment)
                && billing.equals(otherPerson.billing);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, tags, academics,
            guardian, appointment, billing);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("tags", tags)
                .add("academics", academics)
                .add("guardian", guardian.orElse(null))
                .add("appointment", appointment)
                .add("billing", billing)
                .toString();
    }

}
