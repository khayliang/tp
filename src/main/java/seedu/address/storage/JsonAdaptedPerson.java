package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.AppClock;
import seedu.address.commons.util.DateTimeUtil;
import seedu.address.model.academic.Academics;
import seedu.address.model.billing.Billing;
import seedu.address.model.billing.PaymentHistory;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Guardian;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;
import seedu.address.model.person.Phone;
import seedu.address.model.recurrence.Recurrence;
import seedu.address.model.session.Appointment;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";
    private static final String PAYMENT_DATE_MESSAGE_CONSTRAINTS =
            "Payment date must be in ISO 8601 local date format, e.g. 2026-01-13";
    private static final String PAYMENT_DATE_AFTER_TODAY_MESSAGE_CONSTRAINTS =
            "Payment date cannot be later than today.";
    private static final String PAYMENT_DUE_DATE_MESSAGE_CONSTRAINTS =
            "Payment due date must be in ISO 8601 local date format, e.g. 2026-01-13";
    private static final String TUITION_FEE_MESSAGE_CONSTRAINTS =
            "Tuition fee must be a finite non-negative number.";
    private static final String PAYMENT_RECURRENCE_MESSAGE_CONSTRAINTS =
            "Payment recurrence must be one of: WEEKLY, BIWEEKLY, MONTHLY, NONE";

    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final JsonAdaptedAppointment appointment;
    private final String parentName;
    private final String parentPhone;
    private final String parentEmail;
    private final List<String> paymentDates;
    private final String paymentDueDate;
    private final String paymentRecurrence;
    private final double tuitionFee;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    private final JsonAdaptedAcademics academics;

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
            @JsonProperty("email") String email, @JsonProperty("address") String address,
            @JsonProperty("tags") List<JsonAdaptedTag> tags,
            @JsonProperty("academics") JsonAdaptedAcademics academics,
            @JsonProperty("parentName") String parentName,
            @JsonProperty("parentPhone") String parentPhone,
            @JsonProperty("parentEmail") String parentEmail,
            @JsonProperty("appointment") JsonAdaptedAppointment appointment,
            @JsonProperty("paymentDates") List<String> paymentDates,
            @JsonProperty("paymentDueDate") String paymentDueDate,
            @JsonProperty("paymentRecurrence") String paymentRecurrence,
            @JsonProperty("billingMonthlyRate") double tuitionFee) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.parentName = parentName;
        this.parentPhone = parentPhone;
        this.parentEmail = parentEmail;
        this.appointment = appointment;
        this.paymentDates = paymentDates;
        this.paymentDueDate = paymentDueDate;
        this.paymentRecurrence = paymentRecurrence;
        this.tuitionFee = tuitionFee;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        this.academics = academics;
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        this(
            source.getName().fullName,
            source.getPhone().value,
            source.getEmail().value,
            source.getAddress().value,
            source.getTags().stream().map(JsonAdaptedTag::new).collect(Collectors.toList()),
            new JsonAdaptedAcademics(source.getAcademics()),
            extractGuardianName(source),
            extractGuardianPhone(source),
            extractGuardianEmail(source),
            new JsonAdaptedAppointment(source.getAppointment()),
            source.getBilling().getPaymentHistory().getPaidDates().stream()
                .map(value -> value.format(DateTimeUtil.ISO_LOCAL_DATE_STRICT_FORMATTER))
                .collect(Collectors.toList()),
            source.getBilling().getCurrentDueDate().format(DateTimeUtil.ISO_LOCAL_DATE_STRICT_FORMATTER),
            source.getBilling().getRecurrence().name(),
            source.getBilling().getTuitionFee());
    }

    private static String extractGuardianName(Person source) {
        return source.getGuardian()
                .map(Guardian::getName)
                .map(n -> n.fullName)
                .orElse(null);
    }

    private static String extractGuardianPhone(Person source) {
        return source.getGuardian()
                .flatMap(Guardian::getPhone)
                .map(p -> p.value)
                .orElse(null);
    }

    private static String extractGuardianEmail(Person source) {
        return source.getGuardian()
                .flatMap(Guardian::getEmail)
                .map(e -> e.value)
                .orElse(null);
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        return toModelType(AppClock.getClock());
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object
     * using the provided {@code clock} as the source of "today" for date validation.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType(Clock clock) throws IllegalValueException {
        requireNonNull(clock);

        Name modelName = parseCoreName();
        Phone modelPhone = parseCorePhone();
        Email modelEmail = parseCoreEmail();
        Address modelAddress = parseCoreAddress();
        Set<Tag> modelTags = parseTags();
        Academics modelAcademics = parseAcademics();
        Guardian modelGuardian = parseGuardian();
        Appointment modelAppointment = parseAppointment();
        PaymentHistory modelPayment = parsePaymentHistory(clock);
        Billing modelBilling = parseBilling(clock, modelPayment);

        PersonBuilder personBuilder = new PersonBuilder(modelName, modelPhone, modelEmail, modelAddress, modelTags)
            .withAcademics(modelAcademics)
            .withGuardian(modelGuardian)
            .withBilling(modelBilling)
            .withAppointment(modelAppointment);

        return personBuilder.build();
    }

    /**
     * Parses and validates the person's name.
     */
    private Name parseCoreName() throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(name);
    }

    /**
     * Parses and validates the person's phone number.
     */
    private Phone parseCorePhone() throws IllegalValueException {
        if (phone == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(phone);
    }

    /**
     * Parses and validates the person's email.
     */
    private Email parseCoreEmail() throws IllegalValueException {
        if (email == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(email);
    }

    /**
     * Parses and validates the person's address.
     */
    private Address parseCoreAddress() throws IllegalValueException {
        if (address == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(address);
    }

    /**
     * Parses and converts the person's tags.
     */
    private Set<Tag> parseTags() throws IllegalValueException {
        List<Tag> personTags = new ArrayList<>();
        if (tags != null) {
            for (JsonAdaptedTag tag : tags) {
                personTags.add(tag.toModelType());
            }
        }
        return new HashSet<>(personTags);
    }

    /**
     * Parses and converts the person's academics.
     */
    private Academics parseAcademics() throws IllegalValueException {
        return academics != null
                ? academics.toModelType()
                : new Academics(new HashSet<>());
    }

    /**
     * Parses and converts the person's guardian information.
     */
    private Guardian parseGuardian() throws IllegalValueException {
        if (parentName == null) {
            return null;
        }

        if (!Name.isValidName(parentName)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        Name modelParentName = new Name(parentName);

        Phone modelParentPhone = null;
        if (parentPhone != null) {
            if (!Phone.isValidPhone(parentPhone)) {
                throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
            }
            modelParentPhone = new Phone(parentPhone);
        }

        Email modelParentEmail = null;
        if (parentEmail != null) {
            if (!Email.isValidEmail(parentEmail)) {
                throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
            }
            modelParentEmail = new Email(parentEmail);
        }

        return new Guardian(modelParentName, modelParentPhone, modelParentEmail);
    }

    /**
     * Parses and converts the person's appointment container.
     */
    private Appointment parseAppointment() throws IllegalValueException {
        Appointment modelAppointment = Appointment.defaultAppointment();
        if (appointment != null) {
            modelAppointment = appointment.toModelType();
        }
        return modelAppointment;
    }

    /**
     * Parses and converts the person's payment history.
     */
    private PaymentHistory parsePaymentHistory(Clock clock) throws IllegalValueException {
        Set<LocalDate> modelPaidDates = new LinkedHashSet<>();
        LocalDate today = LocalDate.now(clock);
        if (paymentDates != null) {
            for (String dateString : paymentDates) {
                try {
                    LocalDate parsedPaymentDate = LocalDate.parse(dateString,
                            DateTimeUtil.ISO_LOCAL_DATE_STRICT_FORMATTER);
                    if (parsedPaymentDate.isAfter(today)) {
                        throw new IllegalValueException(PAYMENT_DATE_AFTER_TODAY_MESSAGE_CONSTRAINTS);
                    }
                    modelPaidDates.add(parsedPaymentDate);
                } catch (DateTimeParseException e) {
                    throw new IllegalValueException(PAYMENT_DATE_MESSAGE_CONSTRAINTS);
                }
            }
        }

        return !modelPaidDates.isEmpty()
                ? new PaymentHistory(modelPaidDates.toArray(LocalDate[]::new))
                : PaymentHistory.EMPTY;
    }

    /**
     * Parses and converts the person's billing information.
     */
    private Billing parseBilling(Clock clock, PaymentHistory modelPayment) throws IllegalValueException {
        Recurrence modelRecurrence = parsePaymentRecurrence();
        LocalDate modelPaymentDueDate = parsePaymentDueDate(clock);
        validateTuitionFee();

        return new Billing(modelRecurrence, modelPaymentDueDate, tuitionFee, modelPayment);
    }

    private Recurrence parsePaymentRecurrence() throws IllegalValueException {
        Recurrence modelRecurrence = Recurrence.MONTHLY;
        if (paymentRecurrence != null) {
            try {
                modelRecurrence = Recurrence.valueOf(paymentRecurrence);
            } catch (IllegalArgumentException e) {
                throw new IllegalValueException(PAYMENT_RECURRENCE_MESSAGE_CONSTRAINTS);
            }
        }
        return modelRecurrence;
    }

    private LocalDate parsePaymentDueDate(Clock clock) throws IllegalValueException {
        LocalDate modelPaymentDueDate = LocalDate.now(clock).withDayOfMonth(1);
        if (paymentDueDate != null) {
            try {
                modelPaymentDueDate = LocalDate.parse(paymentDueDate, DateTimeUtil.ISO_LOCAL_DATE_STRICT_FORMATTER);
            } catch (DateTimeParseException e) {
                throw new IllegalValueException(PAYMENT_DUE_DATE_MESSAGE_CONSTRAINTS);
            }
        }
        return modelPaymentDueDate;
    }

    private void validateTuitionFee() throws IllegalValueException {
        if (!Double.isFinite(tuitionFee) || tuitionFee < 0) {
            throw new IllegalValueException(TUITION_FEE_MESSAGE_CONSTRAINTS);
        }
    }
}
