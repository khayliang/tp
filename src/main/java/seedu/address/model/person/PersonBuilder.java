package seedu.address.model.person;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.address.model.academic.Academics;
import seedu.address.model.billing.Billing;
import seedu.address.model.session.Attendance;
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
    private Optional<Guardian> guardian;
    private Set<LocalDateTime> appointmentStarts;
    private Attendance attendance;
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
        this.guardian = Optional.empty();
        this.appointmentStarts = new HashSet<>();
        this.attendance = Attendance.EMPTY;
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
        this.guardian = personToCopy.getGuardian();
        this.appointmentStarts = new HashSet<>(personToCopy.getAppointmentStarts());
        this.attendance = personToCopy.getAttendance();
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
     * Replaces the academic profile of the {@code Person} being built.
     *
     * @param academics the new academics profile
     * @return this {@code PersonBuilder} instance for method chaining
     */
    public PersonBuilder withAcademics(Academics academics) {
        this.academics = academics;
        return this;
    }

    /**
     * Sets the {@code Guardian} of the {@code Person} being built.
     *
     * @param guardian the guardian
     * @return this {@code PersonBuilder} instance for method chaining
     */
    public PersonBuilder withGuardian(Guardian guardian) {
        this.guardian = Optional.ofNullable(guardian);
        return this;
    }

    /**
     * Replaces all appointment start times of the {@code Person} being built.
     *
     * @param appointmentStarts the new appointment start times
     * @return this {@code PersonBuilder} instance for method chaining
     */
    public PersonBuilder withAppointmentStarts(LocalDateTime... appointmentStarts) {
        this.appointmentStarts = new HashSet<>(Arrays.asList(appointmentStarts));
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
     * Sets the attendance history of the {@code Person} being built.
     * @param attendance attendance history value object
     * @return this {@code PersonBuilder} instance for method chaining
     */
    public PersonBuilder withAttendance(Attendance attendance) {
        this.attendance = attendance;
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
                guardian,
                appointmentStarts,
                billing,
                attendance);
    }
}
