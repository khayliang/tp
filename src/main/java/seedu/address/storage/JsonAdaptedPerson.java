package seedu.address.storage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.academic.Academics;
import seedu.address.model.billing.Billing;
import seedu.address.model.billing.PaymentHistory;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;
import seedu.address.model.person.Phone;
import seedu.address.model.recurrence.Recurrence;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";
    private static final String APPOINTMENT_START_MESSAGE_CONSTRAINTS =
            "Appointment start date-time must be in ISO 8601 local format, e.g. 2026-01-13T08:00:00";
    private static final String LAST_ATTENDANCE_MESSAGE_CONSTRAINTS =
            "Last attendance date-time must be in ISO 8601 local format, e.g. 2026-01-29T08:00:00";
    private static final String PAYMENT_DATE_MESSAGE_CONSTRAINTS =
            "Payment date must be in ISO 8601 local date format, e.g. 2026-01-13";
    private static final String PAYMENT_DUE_DATE_MESSAGE_CONSTRAINTS =
            "Payment due date must be in ISO 8601 local date format, e.g. 2026-01-13";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE
            .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter DATETIME_FORMATTER =
            DateTimeFormatter.ISO_LOCAL_DATE_TIME.withResolverStyle(ResolverStyle.STRICT);


    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final String appointmentStart;
    private final String lastAttendance;
    private final String parentName; // optional, may be null
    private final String parentPhone; // optional, may be null
    private final String parentEmail; // optional, may be null
    private final List<String> paymentDates;
    private final String paymentDueDate;
    private final String paymentRecurrence;
    private final Double tuitionFee;
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
            @JsonProperty("appointmentStart") String appointmentStart,
            @JsonProperty("paymentDates") List<String> paymentDates,
            @JsonProperty("paymentDueDate") String paymentDueDate,
            @JsonProperty("paymentRecurrence") String paymentRecurrence,
            @JsonProperty("billingMonthlyRate") Double tuitionFee,
            @JsonProperty("lastAttendance") String lastAttendance) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.parentName = parentName;
        this.parentPhone = parentPhone;
        this.parentEmail = parentEmail;
        this.appointmentStart = appointmentStart;
        this.paymentDates = paymentDates;
        this.paymentDueDate = paymentDueDate;
        this.paymentRecurrence = paymentRecurrence;
        this.tuitionFee = tuitionFee;
        this.lastAttendance = lastAttendance;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        this.academics = academics;
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        appointmentStart = source.getAppointmentStart()
                .map(value -> value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .orElse(null);
        lastAttendance = source.getLastAttendance()
            .map(value -> value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            .orElse(null);
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
        academics = new JsonAdaptedAcademics(source.getAcademics());
        parentName = source.getParentName().map(pn -> pn.fullName).orElse(null);
        parentPhone = source.getParentPhone().map(pp -> pp.value).orElse(null);
        parentEmail = source.getParentEmail().map(pe -> pe.value).orElse(null);
        paymentDates = source.getPaymentHistory().getPaidDates().stream()
                .map(value -> value.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .collect(java.util.stream.Collectors.toList());
        paymentDueDate = source.getBilling().getLastDueDate()
                .format(DATE_FORMATTER);
        paymentRecurrence = source.getBilling().getRecurrence().name();
        tuitionFee = source.getBilling().getTuitionFee();
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {

        // ---------- Validate core fields ----------
        if (name == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        // ---------- Tags ----------
        final List<Tag> personTags = new ArrayList<>();
        if (tags != null) {
            for (JsonAdaptedTag tag : tags) {
                personTags.add(tag.toModelType());
            }
        }
        final Set<Tag> modelTags = new HashSet<>(personTags);

        // ---------- Academics ----------
        final Academics modelAcademics = academics != null
                ? academics.toModelType()
                : new Academics(new HashSet<>());

        // ---------- Parent ----------
        Name modelParentName = null;
        if (parentName != null) {
            if (!Name.isValidName(parentName)) {
                throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
            }
            modelParentName = new Name(parentName);
        }

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

        // ---------- Appointment ----------
        LocalDateTime modelAppointmentStart = null;
        if (appointmentStart != null) {
            try {
                modelAppointmentStart = LocalDateTime.parse(appointmentStart, DATETIME_FORMATTER);
            } catch (DateTimeParseException e) {
                throw new IllegalValueException(APPOINTMENT_START_MESSAGE_CONSTRAINTS);
            }
        }

        // ---------- Payment ----------
        Set<LocalDate> modelPaidDates = new java.util.LinkedHashSet<>();
        if (paymentDates != null) {
            for (String dateString : paymentDates) {
                try {
                    modelPaidDates.add(LocalDate.parse(dateString, DATE_FORMATTER));
                } catch (DateTimeParseException e) {
                    throw new IllegalValueException(PAYMENT_DATE_MESSAGE_CONSTRAINTS);
                }
            }
        }

        PaymentHistory modelPayment;
        if (!modelPaidDates.isEmpty()) {
            modelPayment = new PaymentHistory(modelPaidDates.toArray(new LocalDate[0]));
        } else {
            modelPayment = PaymentHistory.EMPTY;
        }

        // ---------- Billing ----------
        Recurrence modelRecurrence = Recurrence.MONTHLY;
        if (paymentRecurrence != null) {
            try {
                modelRecurrence = Recurrence.valueOf(paymentRecurrence);
            } catch (IllegalArgumentException e) {
                throw new IllegalValueException(
                    "Payment recurrence must be one of: WEEKLY, BIWEEKLY, MONTHLY, NONE");
            }
        }

        LocalDate modelPaymentDueDate = LocalDate.now();
        if (paymentDueDate != null) {
            try {
                modelPaymentDueDate = LocalDate.parse(paymentDueDate, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                throw new IllegalValueException(PAYMENT_DUE_DATE_MESSAGE_CONSTRAINTS);
            }
        }

        Billing modelBilling;
        if (tuitionFee == null) {
            modelBilling = Billing.defaultBilling();
        } else {
            modelBilling = new Billing(
                    modelRecurrence, modelPaymentDueDate, tuitionFee, modelPayment);
        }

        // ---------- Last attendance ----------
        LocalDateTime modelLastAttendance = null;
        if (lastAttendance != null) {
            try {
                modelLastAttendance = LocalDateTime.parse(lastAttendance, DATETIME_FORMATTER);
            } catch (DateTimeParseException e) {
                throw new IllegalValueException(LAST_ATTENDANCE_MESSAGE_CONSTRAINTS);
            }
        }

        PersonBuilder personBuilder = new PersonBuilder(modelName, modelPhone, modelEmail, modelAddress, modelTags)
            .withAcademics(modelAcademics)
            .withParentName(modelParentName)
            .withParentPhone(modelParentPhone)
            .withParentEmail(modelParentEmail)
            .withBilling(modelBilling)
            .withLastAttendance(modelLastAttendance);

        if (modelAppointmentStart != null) {
            personBuilder.withAppointmentStarts(modelAppointmentStart);
        }

        return personBuilder.build();
    }
}
