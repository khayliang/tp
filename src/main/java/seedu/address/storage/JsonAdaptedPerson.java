package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.time.Clock;
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
import seedu.address.commons.util.AppClock;
import seedu.address.model.academic.Academics;
import seedu.address.model.attendance.Attendance;
import seedu.address.model.attendance.AttendanceRecords;
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
    private static final String APPOINTMENT_START_MESSAGE_CONSTRAINTS =
            "Appointment start date-time must be in ISO 8601 local format, e.g. 2026-01-13T08:00:00";
    private static final String ATTENDANCE_HISTORY_MESSAGE_CONSTRAINTS =
            "Attendance history date-time must be in ISO 8601 local format, e.g. 2026-01-29T08:00:00";
    private static final String PAYMENT_DATE_MESSAGE_CONSTRAINTS =
            "Payment date must be in ISO 8601 local date format, e.g. 2026-01-13";
    private static final String PAYMENT_DATE_AFTER_TODAY_MESSAGE_CONSTRAINTS =
            "Payment date cannot be later than today.";
    private static final String PAYMENT_DUE_DATE_MESSAGE_CONSTRAINTS =
            "Payment due date must be in ISO 8601 local date format, e.g. 2026-01-13";
    private static final String TUITION_FEE_MESSAGE_CONSTRAINTS =
            "Tuition fee must be a finite non-negative number.";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE
            .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter DATETIME_FORMATTER =
            DateTimeFormatter.ISO_LOCAL_DATE_TIME.withResolverStyle(ResolverStyle.STRICT);


    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final String appointmentStart;
    private final String appointmentNext;
    private final String appointmentRecurrence;
    private final String appointmentDescription;
    private final List<JsonAdaptedAppointmentAttendance> appointmentAttendanceRecords;
    private final List<String> attendanceHistory;
    private final String parentName; // optional, may be null
    private final String parentPhone; // optional, may be null
    private final String parentEmail; // optional, may be null
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
            @JsonProperty("appointmentStart") String appointmentStart,
            @JsonProperty("appointmentNext") String appointmentNext,
            @JsonProperty("appointmentRecurrence") String appointmentRecurrence,
            @JsonProperty("appointmentDescription") String appointmentDescription,
            @JsonProperty("appointmentAttendanceRecords")
            List<JsonAdaptedAppointmentAttendance> appointmentAttendanceRecords,
            @JsonProperty("paymentDates") List<String> paymentDates,
            @JsonProperty("paymentDueDate") String paymentDueDate,
            @JsonProperty("paymentRecurrence") String paymentRecurrence,
            @JsonProperty("billingMonthlyRate") double tuitionFee,
            @JsonProperty("attendanceHistory") List<String> attendanceHistory) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.parentName = parentName;
        this.parentPhone = parentPhone;
        this.parentEmail = parentEmail;
        this.appointmentStart = appointmentStart;
        this.appointmentNext = appointmentNext;
        this.appointmentRecurrence = appointmentRecurrence;
        this.appointmentDescription = appointmentDescription;
        this.appointmentAttendanceRecords = appointmentAttendanceRecords;
        this.paymentDates = paymentDates;
        this.paymentDueDate = paymentDueDate;
        this.paymentRecurrence = paymentRecurrence;
        this.tuitionFee = tuitionFee;
        this.attendanceHistory = attendanceHistory;
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
        appointmentStart = source.getAppointment()
                .map(Appointment::getStart)
                .map(value -> value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .orElse(null);
        appointmentNext = source.getAppointment()
                .map(Appointment::getNext)
                .map(value -> value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .orElse(null);
        appointmentRecurrence = source.getAppointment()
                .map(Appointment::getRecurrence)
                .map(Enum::name)
                .orElse(null);
        appointmentDescription = source.getAppointment()
                .map(Appointment::getDescription)
                .orElse(null);
        appointmentAttendanceRecords = source.getAppointment()
                .map(Appointment::getAttendance)
                .map(AttendanceRecords::getRecords)
                .orElse(List.of())
                .stream()
                .map(JsonAdaptedAppointmentAttendance::new)
                .collect(Collectors.toList());
        attendanceHistory = null;
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
        academics = new JsonAdaptedAcademics(source.getAcademics());
        // Use Optional chaining per review feedback — avoid null checks
        parentName = source.getGuardian()
                .map(Guardian::getName).map(n -> n.fullName).orElse(null);
        parentPhone = source.getGuardian()
                .flatMap(Guardian::getPhone).map(p -> p.value).orElse(null);
        parentEmail = source.getGuardian()
                .flatMap(Guardian::getEmail).map(e -> e.value).orElse(null);
        paymentDates = source.getPaymentHistory().getPaidDates().stream()
                .map(value -> value.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .collect(java.util.stream.Collectors.toList());
        paymentDueDate = source.getBilling().getCurrentDueDate()
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

        // ---------- Guardian (Name is required if guardian exists) ----------
        Guardian modelGuardian = null;
        if (parentName != null) {
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

            modelGuardian = new Guardian(modelParentName, modelParentPhone, modelParentEmail);
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
        LocalDateTime modelAppointmentNext = modelAppointmentStart;
        if (appointmentNext != null) {
            try {
                modelAppointmentNext = LocalDateTime.parse(appointmentNext, DATETIME_FORMATTER);
            } catch (DateTimeParseException e) {
                throw new IllegalValueException(APPOINTMENT_START_MESSAGE_CONSTRAINTS);
            }
        }
        Recurrence modelAppointmentRecurrence = Recurrence.NONE;
        if (appointmentRecurrence != null) {
            try {
                modelAppointmentRecurrence = Recurrence.valueOf(appointmentRecurrence);
            } catch (IllegalArgumentException e) {
                throw new IllegalValueException(
                        "Appointment recurrence must be one of: WEEKLY, BIWEEKLY, MONTHLY, NONE");
            }
        }
        String modelAppointmentDescription = appointmentDescription == null ? "" : appointmentDescription;
        AttendanceRecords modelAppointmentAttendanceRecords = AttendanceRecords.EMPTY;
        if (appointmentAttendanceRecords != null && !appointmentAttendanceRecords.isEmpty()) {
            for (JsonAdaptedAppointmentAttendance attendanceRecord : appointmentAttendanceRecords) {
                modelAppointmentAttendanceRecords = modelAppointmentAttendanceRecords
                        .addAttendance(attendanceRecord.toModelType());
            }
        }

        // ---------- Payment ----------
        Set<LocalDate> modelPaidDates = new java.util.LinkedHashSet<>();
        LocalDate today = LocalDate.now(clock);
        if (paymentDates != null) {
            for (String dateString : paymentDates) {
                try {
                    LocalDate parsedPaymentDate = LocalDate.parse(dateString, DATE_FORMATTER);
                    if (parsedPaymentDate.isAfter(today)) {
                        throw new IllegalValueException(PAYMENT_DATE_AFTER_TODAY_MESSAGE_CONSTRAINTS);
                    }
                    modelPaidDates.add(parsedPaymentDate);
                } catch (DateTimeParseException e) {
                    throw new IllegalValueException(PAYMENT_DATE_MESSAGE_CONSTRAINTS);
                }
            }
        }

        PaymentHistory modelPayment;
        if (!modelPaidDates.isEmpty()) {
            modelPayment = new PaymentHistory(modelPaidDates.toArray(LocalDate[]::new));
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

        LocalDate modelPaymentDueDate = LocalDate.now(clock).withDayOfMonth(1);
        if (paymentDueDate != null) {
            try {
                modelPaymentDueDate = LocalDate.parse(paymentDueDate, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                throw new IllegalValueException(PAYMENT_DUE_DATE_MESSAGE_CONSTRAINTS);
            }
        }

        Billing modelBilling;
        if (!Double.isFinite(tuitionFee) || tuitionFee < 0) {
            throw new IllegalValueException(TUITION_FEE_MESSAGE_CONSTRAINTS);
        } else {
            modelBilling = new Billing(
                    modelRecurrence, modelPaymentDueDate, tuitionFee, modelPayment);
        }

        PersonBuilder personBuilder = new PersonBuilder(modelName, modelPhone, modelEmail, modelAddress, modelTags)
            .withAcademics(modelAcademics)
            .withGuardian(modelGuardian)
            .withBilling(modelBilling);

        if (modelAppointmentStart != null) {
            if ((appointmentAttendanceRecords == null || appointmentAttendanceRecords.isEmpty())
                    && attendanceHistory != null && !attendanceHistory.isEmpty()) {
                for (String attendanceDateTime : attendanceHistory) {
                    try {
                        modelAppointmentAttendanceRecords = modelAppointmentAttendanceRecords.addAttendance(
                                new Attendance(true,
                                        LocalDateTime.parse(attendanceDateTime, DATETIME_FORMATTER).toLocalDate()));
                    } catch (DateTimeParseException e) {
                        throw new IllegalValueException(ATTENDANCE_HISTORY_MESSAGE_CONSTRAINTS);
                    }
                }
            }
            personBuilder.withAppointment(new Appointment(modelAppointmentRecurrence,
                    modelAppointmentStart,
                    modelAppointmentNext != null ? modelAppointmentNext : modelAppointmentStart,
                    modelAppointmentAttendanceRecords,
                    modelAppointmentDescription));
        }

        return personBuilder.build();
    }
}
