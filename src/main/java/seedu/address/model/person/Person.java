package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.subject.Subject;
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

    // Data fields
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();
    private final Set<Subject> subjects = new HashSet<>();
    private final Optional<LocalDateTime> appointmentStart;
    private final Optional<ParentName> parentName;
    private final Optional<LocalDate> paymentDate;

    /**
     * Creates a {@code Person} with the given core fields and tags.
     * Subjects, parent name, appointment start, and payment date are initialized as empty.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        this(name, phone, email, address, tags,
                new HashSet<>(), Optional.empty(), Optional.empty(), Optional.empty());
    }

    /**
     * Every field must be present and not null. parentName defaults to empty.
     */
    public Person(Name name, Phone phone, Email email, Address address,
                  Set<Tag> tags, Set<Subject> subjects,
                  Optional<ParentName> parentName,
                  Optional<LocalDateTime> appointmentStart,
                  Optional<LocalDate> paymentDate) {

        requireAllNonNull(name, phone, email, address, tags, subjects,
                parentName, appointmentStart, paymentDate);

        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.subjects.addAll(subjects);
        this.parentName = parentName;
        this.appointmentStart = appointmentStart;
        this.paymentDate = paymentDate;
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

    public Optional<LocalDateTime> getAppointmentStart() {
        return appointmentStart;
    }

    public Optional<LocalDate> getPaymentDate() {
        return paymentDate;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns an immutable subject set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Subject> getSubjects() {
        return Collections.unmodifiableSet(subjects);
    }

    /**
     * Returns the parent name wrapped in an Optional, or empty if not set.
     */
    public Optional<ParentName> getParentName() {
        return parentName;
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
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
                && subjects.equals(otherPerson.subjects)
                && parentName.equals(otherPerson.parentName)
                && appointmentStart.equals(otherPerson.appointmentStart)
                && paymentDate.equals(otherPerson.paymentDate);

    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, tags, subjects,
                parentName, appointmentStart, paymentDate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("tags", tags)
                .add("subjects", subjects)
                .add("parentName", parentName.orElse(null))
                .add("appointmentStart", appointmentStart)
                .add("paymentDate", paymentDate)
                .toString();
    }

}
