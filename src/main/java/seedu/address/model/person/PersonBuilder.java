package seedu.address.model.person;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Builder class for constructing {@code Person} objects safely.
 */
public class PersonBuilder {

    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private Set<Tag> tags;
    private Optional<Name> parentName;
    private Optional<Phone> parentPhone;
    private Optional<Email> parentEmail;
    private Optional<LocalDateTime> appointmentStart;
    private Optional<LocalDate> paymentDate;
    private Optional<LocalDateTime> lastAttendance;

    /**
     * Creates a builder initialized with required person fields.
     * Optional fields default to empty.
     */
    public PersonBuilder(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags = new HashSet<>(tags);
        this.parentName = Optional.empty();
        this.parentPhone = Optional.empty();
        this.parentEmail = Optional.empty();
        this.appointmentStart = Optional.empty();
        this.paymentDate = Optional.empty();
        this.lastAttendance = Optional.empty();
    }

    /**
     * Creates a builder initialized with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        this.name = personToCopy.getName();
        this.phone = personToCopy.getPhone();
        this.email = personToCopy.getEmail();
        this.address = personToCopy.getAddress();
        this.tags = new HashSet<>(personToCopy.getTags());
        this.parentName = personToCopy.getParentName();
        this.parentPhone = personToCopy.getParentPhone();
        this.parentEmail = personToCopy.getParentEmail();
        this.appointmentStart = personToCopy.getAppointmentStart();
        this.paymentDate = personToCopy.getPaymentDate();
        this.lastAttendance = personToCopy.getLastAttendance();
    }

    /**
     * Sets the {@code Name} of the {@code Person} being built.
     *
     * @param name the new name
     * @return this {@code PersonBuilder} instance for method chaining
     */
    public PersonBuilder withName(Name name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} being built.
     *
     * @param phone the new phone number
     * @return this {@code PersonBuilder} instance for method chaining
     */
    public PersonBuilder withPhone(Phone phone) {
        this.phone = phone;
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} being built.
     *
     * @param email the new email address
     * @return this {@code PersonBuilder} instance for method chaining
     */
    public PersonBuilder withEmail(Email email) {
        this.email = email;
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} being built.
     *
     * @param address the new address
     * @return this {@code PersonBuilder} instance for method chaining
     */
    public PersonBuilder withAddress(Address address) {
        this.address = address;
        return this;
    }

    /**
     * Replaces the tag set of the {@code Person} being built.
     * A defensive copy of the provided tag set is created.
     *
     * @param tags the new set of tags
     * @return this {@code PersonBuilder} instance for method chaining
     */
    public PersonBuilder withTags(Set<Tag> tags) {
        this.tags = new HashSet<>(tags);
        return this;
    }

    /**
     * Sets the parent's {@code Name} of the {@code Person} being built.
     *
     * @param parentName the optional parent name
     * @return this {@code PersonBuilder} instance for method chaining
     */
    public PersonBuilder withParentName(Optional<Name> parentName) {
        this.parentName = parentName;
        return this;
    }

    /**
     * Sets the parent's {@code Phone} of the {@code Person} being built.
     *
     * @param parentPhone the optional parent phone
     * @return this {@code PersonBuilder} instance for method chaining
     */
    public PersonBuilder withParentPhone(Optional<Phone> parentPhone) {
        this.parentPhone = parentPhone;
        return this;
    }

    /**
     * Sets the parent's {@code Email} of the {@code Person} being built.
     *
     * @param parentEmail the optional parent email
     * @return this {@code PersonBuilder} instance for method chaining
     */
    public PersonBuilder withParentEmail(Optional<Email> parentEmail) {
        this.parentEmail = parentEmail;
        return this;
    }

    /**
     * Sets the appointment start time of the {@code Person} being built.
     *
     * @param appointmentStart the optional appointment start time
     * @return this {@code PersonBuilder} instance for method chaining
     */
    public PersonBuilder withAppointmentStart(Optional<LocalDateTime> appointmentStart) {
        this.appointmentStart = appointmentStart;
        return this;
    }

    /**
     * Sets the payment date of the {@code Person} being built.
     *
     * @param paymentDate the optional payment date
     * @return this {@code PersonBuilder} instance for method chaining
     */
    public PersonBuilder withPaymentDate(Optional<LocalDate> paymentDate) {
        this.paymentDate = paymentDate;
        return this;
    }

    /**
     * Sets the last attendance time of the {@code Person} being built.
     *
     * @param lastAttendance the optional last attendance time
     * @return this {@code PersonBuilder} instance for method chaining
     */
    public PersonBuilder withLastAttendance(Optional<LocalDateTime> lastAttendance) {
        this.lastAttendance = lastAttendance;
        return this;
    }

    /**
     * Builds a {@code Person} with the current builder state.
     */
    public Person build() {
        return new Person(
                name,
                phone,
                email,
                address,
                tags,
                parentName,
                parentPhone,
                parentEmail,
                appointmentStart,
                paymentDate,
                lastAttendance);
    }
}
