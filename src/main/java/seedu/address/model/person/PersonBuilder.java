package seedu.address.model.person;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.address.model.academic.Academics;
import seedu.address.model.billing.Billing;
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
    private Academics academics;
    private Optional<Name> parentName;
    private Optional<Phone> parentPhone;
    private Optional<Email> parentEmail;
    private Optional<LocalDateTime> appointmentStart;
    private Optional<LocalDateTime> lastAttendance;
    private Billing billing;

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
        this.academics = new Academics();
        this.parentName = Optional.empty();
        this.parentPhone = Optional.empty();
        this.parentEmail = Optional.empty();
        this.appointmentStart = Optional.empty();
        this.lastAttendance = Optional.empty();
        this.billing = Billing.defaultBilling();
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
        this.academics = personToCopy.getAcademics();
        this.parentName = personToCopy.getParentName();
        this.parentPhone = personToCopy.getParentPhone();
        this.parentEmail = personToCopy.getParentEmail();
        this.appointmentStart = personToCopy.getAppointmentStart();
        this.lastAttendance = personToCopy.getLastAttendance();
        this.billing = personToCopy.getBilling();
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
     * Replaces the subject set of the {@code Person} being built.
     * A defensive copy of the provided subject set is created.
     *
     * @param academics
     * @return this {@code PersonBuilder} instance for method chaining
     */
    public PersonBuilder withAcademics(Academics academics) {
        this.academics = academics;
        return this;
    }

    /**
     * Sets the parent's {@code Name} of the {@code Person} being built.
     *
     * @param parentName the parent name
     * @return this {@code PersonBuilder} instance for method chaining
     */
    public PersonBuilder withParentName(Name parentName) {
        this.parentName = Optional.ofNullable(parentName);
        return this;
    }

    /**
     * Sets the parent's {@code Phone} of the {@code Person} being built.
     *
     * @param parentPhone the parent phone
     * @return this {@code PersonBuilder} instance for method chaining
     */
    public PersonBuilder withParentPhone(Phone parentPhone) {
        this.parentPhone = Optional.ofNullable(parentPhone);
        return this;
    }

    /**
     * Sets the parent's {@code Email} of the {@code Person} being built.
     *
     * @param parentEmail the parent email
     * @return this {@code PersonBuilder} instance for method chaining
     */
    public PersonBuilder withParentEmail(Email parentEmail) {
        this.parentEmail = Optional.ofNullable(parentEmail);
        return this;
    }

    /**
     * Sets the appointment start time of the {@code Person} being built.
     *
     * @param appointmentStart the appointment start time
     * @return this {@code PersonBuilder} instance for method chaining
     */
    public PersonBuilder withAppointmentStart(LocalDateTime appointmentStart) {
        this.appointmentStart = Optional.ofNullable(appointmentStart);
        return this;
    }

    /**
     * Sets the billing information of the {@code Person} being built.
     * @param billing the new billing cycle
     * @return this {@code PersonBuilder} instance for method chaining
     */
    public PersonBuilder withBilling(Billing billing) {
        this.billing = billing;
        return this;
    }

    /**
     * Sets the last attendance time of the {@code Person} being built.
     * @param lastAttendance the optional last attendance time
     * @return this {@code PersonBuilder} instance for method chaining
     */
    public PersonBuilder withLastAttendance(LocalDateTime lastAttendance) {
        this.lastAttendance = Optional.ofNullable(lastAttendance);
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
                academics,
                parentName,
                parentPhone,
                parentEmail,
                appointmentStart,
                billing,
                lastAttendance);
    }
}
