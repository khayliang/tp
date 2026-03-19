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
    private final Optional<LocalDateTime> appointmentStart;
    private final Optional<LocalDateTime> lastAttendance;
    private final Optional<Name> parentName;
    private final Optional<Phone> parentPhone;
    private final Optional<Email> parentEmail;
    private final Optional<LocalDate> paymentDate;

    /**
     * Every field must be present and not null.
     * Fields other than personal details (name, phone, email, and address)
     * are optional and can be empty.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        this(name, phone, email, address, tags, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty());
    }

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags, Optional<Name> parentName,
            Optional<LocalDateTime> appointmentStart, Optional<LocalDate> paymentDate) {
        this(name, phone, email, address, tags, parentName, Optional.empty(), Optional.empty(), appointmentStart,
                paymentDate, Optional.empty());
    }

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags,
            Optional<Name> parentName, Optional<Phone> parentPhone, Optional<Email> parentEmail,
            Optional<LocalDateTime> appointmentStart, Optional<LocalDate> paymentDate) {
        this(name, phone, email, address, tags, parentName, parentPhone, parentEmail, appointmentStart, paymentDate,
                Optional.empty());
    }

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags,
            Optional<Name> parentName, Optional<Phone> parentPhone, Optional<Email> parentEmail,
            Optional<LocalDateTime> appointmentStart, Optional<LocalDate> paymentDate,
            Optional<LocalDateTime> lastAttendance) {
        requireAllNonNull(name, phone, email, address, tags,
                parentName, parentPhone, parentEmail, appointmentStart, paymentDate, lastAttendance);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.appointmentStart = appointmentStart;
        this.lastAttendance = lastAttendance;
        this.parentName = parentName;
        this.parentPhone = parentPhone;
        this.parentEmail = parentEmail;
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

    public Optional<LocalDateTime> getLastAttendance() {
        return lastAttendance;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns the parent name wrapped in an Optional, or empty if not set.
     */
    public Optional<Name> getParentName() {
        return parentName;
    }

    /**
     * Returns the parent phone wrapped in an Optional, or empty if not set.
     */
    public Optional<Phone> getParentPhone() {
        return parentPhone;
    }

    /**
     * Returns the parent email wrapped in an Optional, or empty if not set.
     */
    public Optional<Email> getParentEmail() {
        return parentEmail;
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null && otherPerson.getName().equals(getName());
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
        return name.equals(otherPerson.name) && phone.equals(otherPerson.phone) && email.equals(otherPerson.email)
                && address.equals(otherPerson.address) && tags.equals(otherPerson.tags)
                && parentName.equals(otherPerson.parentName)
                && parentPhone.equals(otherPerson.parentPhone)
                && parentEmail.equals(otherPerson.parentEmail)
                && appointmentStart.equals(otherPerson.appointmentStart)
                && paymentDate.equals(otherPerson.paymentDate)
                && lastAttendance.equals(otherPerson.lastAttendance);

    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing
        // your own
        return Objects.hash(name, phone, email, address, tags, parentName, parentPhone, parentEmail, appointmentStart,
                paymentDate, lastAttendance);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("name", name).add("phone", phone).add("email", email)
                .add("address", address).add("tags", tags)
                .add("parentName", parentName.orElse(null))
                .add("parentPhone", parentPhone.orElse(null))
                .add("parentEmail", parentEmail.orElse(null))
                .add("appointmentStart", appointmentStart)
                .add("paymentDate", paymentDate)
                .add("lastAttendance", lastAttendance)
                .toString();
    }

}
