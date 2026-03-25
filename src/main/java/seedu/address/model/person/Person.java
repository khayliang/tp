package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.academic.Academics;
import seedu.address.model.billing.Billing;
import seedu.address.model.billing.PaymentHistory;
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
    private final Set<LocalDateTime> appointmentStarts = new TreeSet<>();
    private final Optional<LocalDateTime> lastAttendance;
    private final Optional<Name> parentName;
    private final Optional<Phone> parentPhone;
    private final Optional<Email> parentEmail;
    private final Billing billing;

    /**
     * Creates a {@code Person} with the given core fields and tags.
     * Fields other than personal details (name, phone, email, and address)
     * are optional and can be empty.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        requireAllNonNull(name, phone, email, address, tags);

        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);

        this.academics = new Academics();

        this.parentName = Optional.empty();
        this.parentPhone = Optional.empty();
        this.parentEmail = Optional.empty();
        this.billing = Billing.defaultBilling();
        this.lastAttendance = Optional.empty();
    }

    /**
     * Every field must be present and not null. parentName defaults to empty.
     */
    public Person(Name name, Phone phone, Email email, Address address,
                  Set<Tag> tags, Academics academics,
                  Optional<Name> parentName, Optional<Phone> parentPhone, Optional<Email> parentEmail,
                  Set<LocalDateTime> appointmentStarts,
                  Billing billing,
                  Optional<LocalDateTime> lastAttendance) {

        requireAllNonNull(name, phone, email, address, tags, academics,
                parentName, parentPhone, parentEmail,
                appointmentStarts, billing, lastAttendance);

        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);

        this.academics = academics;

        this.parentName = parentName;
        this.parentPhone = parentPhone;
        this.parentEmail = parentEmail;
        appointmentStarts.forEach(Objects::requireNonNull);
        this.appointmentStarts.addAll(appointmentStarts);
        this.lastAttendance = lastAttendance;
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
     * Returns the earliest appointment start, or empty if no appointments exist.
     */
    public Optional<LocalDateTime> getAppointmentStart() {
        return appointmentStarts.stream().min(LocalDateTime::compareTo);
    }

    /**
     * Returns an immutable appointment set.
     */
    public Set<LocalDateTime> getAppointmentStarts() {
        return Collections.unmodifiableSet(appointmentStarts);
    }

    public Billing getBilling() {
        return billing;
    }

    public PaymentHistory getPaymentHistory() {
        return billing.getPaymentHistory();
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

    public Academics getAcademics() {
        return academics;
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
     * Returns an immutable {@code Billing} object with updated payment history
     * and advanced billing cycle
     * @param paymentDate A valid {@code LocalDate}
     * @return {@code Billing} object
     */
    public Billing recordFeesPaidAndAdvanceBilling(LocalDate paymentDate) {
        Billing updatedBilling = billing.recordTuitionPaid(paymentDate);
        return updatedBilling.advanceDueDate();
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
                && parentName.equals(otherPerson.parentName)
                && parentPhone.equals(otherPerson.parentPhone)
                && parentEmail.equals(otherPerson.parentEmail)
                && appointmentStarts.equals(otherPerson.appointmentStarts)
                && billing.equals(otherPerson.billing)
                && lastAttendance.equals(otherPerson.lastAttendance);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, tags, academics,
                parentName, parentPhone, parentEmail,
                appointmentStarts, billing, lastAttendance);
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
                .add("parentName", parentName.orElse(null))
                .add("parentPhone", parentPhone.orElse(null))
                .add("parentEmail", parentEmail.orElse(null))
                .add("appointmentStart", getAppointmentStart())
                .add("appointmentStarts", appointmentStarts)
                .add("billing", billing)
                .add("lastAttendance", lastAttendance)
                .toString();
    }

}
