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

import seedu.address.commons.util.DateTimeUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.academic.Academics;
import seedu.address.model.billing.Billing;
import seedu.address.model.billing.PaymentHistory;
import seedu.address.model.session.Attendance;
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
    private final Attendance attendance;
    private final Optional<Guardian> guardian;
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

        this.guardian = Optional.empty();
        this.billing = Billing.defaultBilling();
        this.attendance = Attendance.EMPTY;
    }

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address,
                  Set<Tag> tags, Academics academics,
                  Optional<Guardian> guardian,
                  Set<LocalDateTime> appointmentStarts,
                  Billing billing, Attendance attendance) {

        requireAllNonNull(name, phone, email, address, tags, academics,
                guardian, appointmentStarts, billing, attendance);

        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);

        this.academics = academics;

        this.guardian = guardian;
        appointmentStarts.forEach(Objects::requireNonNull);
        appointmentStarts.stream()
            .map(DateTimeUtil::normalizeToMinute)
            .forEach(this.appointmentStarts::add);
        this.attendance = attendance;
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

    public Attendance getAttendance() {
        return attendance;
    }

    /**
     * Returns attendance history in insertion order.
     */
    public Set<LocalDateTime> getAttendanceHistory() {
        return attendance.getHistory();
    }

    public Optional<LocalDateTime> getLastAttendance() {
        return attendance.getLastAttendance();
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
     * Returns the guardian wrapped in an Optional, or empty if not set.
     */
    public Optional<Guardian> getGuardian() {
        return guardian;
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
     * and advanced billing cycle if paymentDate is before or on today's date in SG timezone
     * @param paymentDate A valid {@code LocalDate}
     * @return {@code Billing} object
     */
    public Billing recordFeesPaidAndAdvanceBilling(LocalDate paymentDate) {
        Billing updatedBilling = billing.recordTuitionPaid(paymentDate);
        return updatedBilling.advanceDueDate();
    }

    /**
     * Returns an immutable {@code Billing} object with updated payment history after deleting
     * a recorded payment date. Due date is rolled back one recurrence cycle only when deleting
     * the latest chronological payment date.
     * @param paymentDate A valid {@code LocalDate}
     * @return updated {@code Billing} object
     * @throws IllegalArgumentException if paymentDate is not recorded
     */
    public Billing deleteRecordedPayment(LocalDate paymentDate) {
        return billing.deleteRecordedPayment(paymentDate);
    }

    /**
     * Returns attendance history with the provided attendance date-time appended.
     */
    public Attendance addAttendance(LocalDateTime attendanceDateTime) {
        requireAllNonNull(attendanceDateTime);
        return attendance.addAttendance(attendanceDateTime);
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
                && appointmentStarts.equals(otherPerson.appointmentStarts)
                && billing.equals(otherPerson.billing)
                && attendance.equals(otherPerson.attendance);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, tags, academics,
                guardian, appointmentStarts, billing, attendance);
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
                .add("appointmentStart", getAppointmentStart())
                .add("appointmentStarts", appointmentStarts)
                .add("billing", billing)
                .add("attendance", attendance)
                .toString();
    }

}
