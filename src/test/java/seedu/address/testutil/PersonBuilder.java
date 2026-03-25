package seedu.address.testutil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import seedu.address.model.academic.Academics;
import seedu.address.model.billing.Billing;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Guardian;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final LocalDate DEFAULT_PAYMENT_DATE = LocalDate.of(2026, 02, 10);

    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private Set<Tag> tags;
    private Academics academics;
    private Name parentName;
    private Phone parentPhone;
    private Email parentEmail;
    private LocalDateTime appointmentStart;
    private Billing billing;
    private LocalDateTime lastAttendance;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        tags = new HashSet<>();
        academics = new Academics();
        parentName = null;
        parentPhone = null;
        parentEmail = null;
        appointmentStart = null;
        billing = Billing.defaultBilling();
        lastAttendance = null;
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        address = personToCopy.getAddress();
        tags = new HashSet<>(personToCopy.getTags());
        academics = personToCopy.getAcademics();
        Guardian guardianToCopy = personToCopy.getGuardian().orElse(null);
        parentName = guardianToCopy != null ? guardianToCopy.getName() : null;
        parentPhone = guardianToCopy != null ? guardianToCopy.getPhone().orElse(null) : null;
        parentEmail = guardianToCopy != null ? guardianToCopy.getEmail().orElse(null) : null;
        appointmentStart = personToCopy.getAppointmentStart().orElse(null);
        billing = personToCopy.getBilling();
        lastAttendance = personToCopy.getLastAttendance().orElse(null);
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Academics} of the {@code Person} that we are building.
     */
    public PersonBuilder withAcademics(Academics academics) {
        this.academics = academics;
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public PersonBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the parent's {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withParentName(String name) {
        this.parentName = new Name(name);
        return this;
    }

    /**
     * Sets the parent's {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withParentPhone(String phone) {
        this.parentPhone = new Phone(phone);
        return this;
    }

    /**
     * Sets the parent's {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withParentEmail(String email) {
        this.parentEmail = new Email(email);
        return this;
    }

    /**
     * Sets the appointment start date-time of the {@code Person} that we are building.
     */
    public PersonBuilder withAppointmentStart(String appointmentStart) {
        this.appointmentStart = LocalDateTime.parse(appointmentStart);
        return this;
    }

    /**
     * Sets the last attendance date-time of the {@code Person} that we are building.
     */
    public PersonBuilder withLastAttendance(String lastAttendance) {
        this.lastAttendance = LocalDateTime.parse(lastAttendance);
        return this;
    }

    /**
     * Sets the {@code Billing} information of the {@code Person} that we are building.
     */
    public PersonBuilder withBilling(Billing billing) {
        this.billing = billing;
        return this;
    }

    /**
     * Builds a {@code Person} with the current builder state.
     */
    public Person build() {
        Guardian guardian = null;
        if (parentName != null) {
            guardian = new Guardian(parentName, parentPhone, parentEmail);
        }
        return new seedu.address.model.person.PersonBuilder(name, phone, email, address, tags)
                .withAcademics(academics)
                .withGuardian(guardian)
                .withAppointmentStarts(appointmentStart == null
                        ? new java.time.LocalDateTime[0]
                        : new java.time.LocalDateTime[]{appointmentStart})
                .withBilling(billing)
                .withLastAttendance(lastAttendance)
                .build();
    }
}
