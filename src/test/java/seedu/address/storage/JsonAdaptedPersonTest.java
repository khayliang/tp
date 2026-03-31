package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.academic.Academics;
import seedu.address.model.academic.Subject;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

public class JsonAdaptedPersonTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_APPOINTMENT_START = "2026-13-40T25:00:00";
    private static final List<String> INVALID_PAYMENT_DATES =
            List.of("2026-13-40", "2026-10-10T10:00:00");
    private static final String INVALID_PAYMENT_DUE_DATE = "2026-02-29T02:00:00";
    private static final String INVALID_PAYMENT_RECURRENCE = "hello";
    private static final Double INVALID_TUITION_FEE = -5.0;
    private static final String INVALID_ATTENDANCE_ENTRY = "2026-01-29T33:00:00";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final String VALID_APPOINTMENT_START = "2026-01-13T08:00:00";
    private static final List<String> VALID_PAYMENT_DATES = List.of("2026-01-13", "2026-02-16");
    private static final String VALID_PAYMENT_DUE_DATE = "2026-03-30";
    private static final String VALID_PAYMENT_RECURRENCE = "MONTHLY";
    private static final Double VALID_TUITION_FEE = 25.0;
    private static final String VALID_ATTENDANCE_ENTRY = "2026-01-29T08:00:00";
    private static final String FUTURE_PAYMENT_DATE = "2026-03-29";
    private static final List<String> VALID_ATTENDANCE_HISTORY = List.of(
            "2026-01-28T08:00:00",
            VALID_ATTENDANCE_ENTRY);
    private static final Clock FIXED_CLOCK = Clock.fixed(
            Instant.parse("2026-03-28T12:00:00Z"),
            ZoneId.of("Asia/Singapore"));
    private static final String VALID_PARENT_NAME = BENSON.getGuardian()
            .map(g -> g.getName().fullName).orElse(null);
    private static final String VALID_PARENT_PHONE = BENSON.getGuardian()
            .flatMap(g -> g.getPhone()).map(p -> p.value).orElse(null);
    private static final String VALID_PARENT_EMAIL = BENSON.getGuardian()
            .flatMap(g -> g.getEmail()).map(e -> e.value).orElse(null);
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream().map(JsonAdaptedTag::new)
            .collect(Collectors.toList());
    private static final JsonAdaptedAcademics VALID_ACADEMICS = new JsonAdaptedAcademics(BENSON.getAcademics());

    private static JsonAdaptedPerson makeJsonAdaptedPerson(String name, String phone, String email, String address,
            List<JsonAdaptedTag> tags, JsonAdaptedAcademics academics,
            String parentName, String parentPhone, String parentEmail, String appointmentStart,
            List<String> paymentDates, String paymentDueDate, String paymentRecurrence,
            Double tuitionFee, List<String> attendanceHistory) {
        return new JsonAdaptedPerson(name, phone, email, address, tags, academics, parentName, parentPhone,
                parentEmail, appointmentStart, null, null, null, null, paymentDates, paymentDueDate,
                paymentRecurrence, tuitionFee, attendanceHistory);
    }

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                makeJsonAdaptedPerson(INVALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_TAGS, VALID_ACADEMICS,
                        VALID_PARENT_NAME, VALID_PARENT_PHONE, VALID_PARENT_EMAIL,
                        VALID_APPOINTMENT_START,
                        VALID_PAYMENT_DATES, VALID_PAYMENT_DUE_DATE,
                        VALID_PAYMENT_RECURRENCE, VALID_TUITION_FEE,
                        List.of(VALID_ATTENDANCE_ENTRY));
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                makeJsonAdaptedPerson(null, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_TAGS, VALID_ACADEMICS,
                        VALID_PARENT_NAME, VALID_PARENT_PHONE, VALID_PARENT_EMAIL,
                        VALID_APPOINTMENT_START,
                        VALID_PAYMENT_DATES, VALID_PAYMENT_DUE_DATE,
                        VALID_PAYMENT_RECURRENCE, VALID_TUITION_FEE,
                        List.of(VALID_ATTENDANCE_ENTRY));
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                makeJsonAdaptedPerson(VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_TAGS, VALID_ACADEMICS,
                        VALID_PARENT_NAME, VALID_PARENT_PHONE, VALID_PARENT_EMAIL,
                        VALID_APPOINTMENT_START,
                        VALID_PAYMENT_DATES, VALID_PAYMENT_DUE_DATE,
                        VALID_PAYMENT_RECURRENCE, VALID_TUITION_FEE,
                        List.of(VALID_ATTENDANCE_ENTRY));
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                makeJsonAdaptedPerson(VALID_NAME, null, VALID_EMAIL, VALID_ADDRESS,
                        VALID_TAGS, VALID_ACADEMICS,
                        VALID_PARENT_NAME, VALID_PARENT_PHONE, VALID_PARENT_EMAIL,
                        VALID_APPOINTMENT_START,
                        VALID_PAYMENT_DATES, VALID_PAYMENT_DUE_DATE,
                        VALID_PAYMENT_RECURRENCE, VALID_TUITION_FEE,
                        List.of(VALID_ATTENDANCE_ENTRY));
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                makeJsonAdaptedPerson(VALID_NAME, VALID_PHONE, INVALID_EMAIL, VALID_ADDRESS,
                        VALID_TAGS, VALID_ACADEMICS,
                        VALID_PARENT_NAME, VALID_PARENT_PHONE, VALID_PARENT_EMAIL,
                        VALID_APPOINTMENT_START,
                        VALID_PAYMENT_DATES, VALID_PAYMENT_DUE_DATE,
                        VALID_PAYMENT_RECURRENCE, VALID_TUITION_FEE,
                        List.of(VALID_ATTENDANCE_ENTRY));
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                makeJsonAdaptedPerson(VALID_NAME, VALID_PHONE, null, VALID_ADDRESS,
                        VALID_TAGS, VALID_ACADEMICS,
                        VALID_PARENT_NAME, VALID_PARENT_PHONE, VALID_PARENT_EMAIL,
                        VALID_APPOINTMENT_START,
                        VALID_PAYMENT_DATES, VALID_PAYMENT_DUE_DATE,
                        VALID_PAYMENT_RECURRENCE, VALID_TUITION_FEE,
                        List.of(VALID_ATTENDANCE_ENTRY));
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                makeJsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, INVALID_ADDRESS,
                        VALID_TAGS, VALID_ACADEMICS,
                        VALID_PARENT_NAME, VALID_PARENT_PHONE, VALID_PARENT_EMAIL,
                        VALID_APPOINTMENT_START,
                        VALID_PAYMENT_DATES, VALID_PAYMENT_DUE_DATE,
                        VALID_PAYMENT_RECURRENCE, VALID_TUITION_FEE,
                        List.of(VALID_ATTENDANCE_ENTRY));

        String expectedMessage = Address.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                makeJsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, null,
                        VALID_TAGS, VALID_ACADEMICS,
                        VALID_PARENT_NAME, VALID_PARENT_PHONE, VALID_PARENT_EMAIL,
                        VALID_APPOINTMENT_START,
                        VALID_PAYMENT_DATES, VALID_PAYMENT_DUE_DATE,
                        VALID_PAYMENT_RECURRENCE, VALID_TUITION_FEE,
                        List.of(VALID_ATTENDANCE_ENTRY));

        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedPerson person =
                makeJsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        invalidTags, VALID_ACADEMICS,
                        VALID_PARENT_NAME, VALID_PARENT_PHONE, VALID_PARENT_EMAIL,
                        VALID_APPOINTMENT_START,
                        VALID_PAYMENT_DATES, VALID_PAYMENT_DUE_DATE,
                        VALID_PAYMENT_RECURRENCE, VALID_TUITION_FEE,
                        List.of(VALID_ATTENDANCE_ENTRY));

        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_invalidAppointmentStart_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                makeJsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_TAGS, VALID_ACADEMICS,
                        VALID_PARENT_NAME, VALID_PARENT_PHONE, VALID_PARENT_EMAIL,
                        INVALID_APPOINTMENT_START,
                        VALID_PAYMENT_DATES, VALID_PAYMENT_DUE_DATE,
                        VALID_PAYMENT_RECURRENCE, VALID_TUITION_FEE,
                        List.of(VALID_ATTENDANCE_ENTRY));
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_invalidPaymentDates_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                makeJsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_TAGS, VALID_ACADEMICS,
                        VALID_PARENT_NAME, VALID_PARENT_PHONE, VALID_PARENT_EMAIL,
                        VALID_APPOINTMENT_START,
                        INVALID_PAYMENT_DATES, VALID_PAYMENT_DUE_DATE,
                        VALID_PAYMENT_RECURRENCE, VALID_TUITION_FEE,
                        List.of(VALID_ATTENDANCE_ENTRY));
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_futurePaymentDate_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                makeJsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_TAGS, VALID_ACADEMICS,
                        VALID_PARENT_NAME, VALID_PARENT_PHONE, VALID_PARENT_EMAIL,
                        VALID_APPOINTMENT_START,
                        List.of(FUTURE_PAYMENT_DATE), VALID_PAYMENT_DUE_DATE,
                        VALID_PAYMENT_RECURRENCE, VALID_TUITION_FEE,
                        List.of(VALID_ATTENDANCE_ENTRY));
        assertThrows(IllegalValueException.class, () -> person.toModelType(FIXED_CLOCK));
    }

    @Test
    public void toModelType_invalidPaymentDueDate_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                makeJsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_TAGS, VALID_ACADEMICS,
                        VALID_PARENT_NAME, VALID_PARENT_PHONE, VALID_PARENT_EMAIL,
                        VALID_APPOINTMENT_START,
                        VALID_PAYMENT_DATES, INVALID_PAYMENT_DUE_DATE,
                        VALID_PAYMENT_RECURRENCE, VALID_TUITION_FEE,
                        List.of(VALID_ATTENDANCE_ENTRY));
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_invalidPaymentRecurrence_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                makeJsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_TAGS, VALID_ACADEMICS,
                        VALID_PARENT_NAME, VALID_PARENT_PHONE, VALID_PARENT_EMAIL,
                        VALID_APPOINTMENT_START,
                        VALID_PAYMENT_DATES, VALID_PAYMENT_DUE_DATE,
                        INVALID_PAYMENT_RECURRENCE, VALID_TUITION_FEE,
                        List.of(VALID_ATTENDANCE_ENTRY));
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_invalidTutionFee_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                makeJsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_TAGS, VALID_ACADEMICS,
                        VALID_PARENT_NAME, VALID_PARENT_PHONE, VALID_PARENT_EMAIL,
                        VALID_APPOINTMENT_START,
                        VALID_PAYMENT_DATES, VALID_PAYMENT_DUE_DATE,
                        VALID_PAYMENT_RECURRENCE, INVALID_TUITION_FEE,
                        List.of(VALID_ATTENDANCE_ENTRY));
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_infinityTutionFee_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                makeJsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_TAGS, VALID_ACADEMICS,
                        VALID_PARENT_NAME, VALID_PARENT_PHONE, VALID_PARENT_EMAIL,
                        VALID_APPOINTMENT_START,
                        VALID_PAYMENT_DATES, VALID_PAYMENT_DUE_DATE,
                        VALID_PAYMENT_RECURRENCE, Double.POSITIVE_INFINITY,
                        List.of(VALID_ATTENDANCE_ENTRY));
        assertThrows(IllegalValueException.class, person::toModelType);
    }


    @Test
    public void toModelType_invalidAttendanceHistory_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                makeJsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_TAGS, VALID_ACADEMICS,
                        VALID_PARENT_NAME, VALID_PARENT_PHONE, VALID_PARENT_EMAIL,
                        VALID_APPOINTMENT_START,
                        VALID_PAYMENT_DATES, VALID_PAYMENT_DUE_DATE,
                        VALID_PAYMENT_RECURRENCE, VALID_TUITION_FEE,
                        List.of(INVALID_ATTENDANCE_ENTRY));
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_validAttendanceHistory_usesAttendanceHistory() throws Exception {
        JsonAdaptedPerson person = makeJsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_TAGS, VALID_ACADEMICS,
                VALID_PARENT_NAME, VALID_PARENT_PHONE, VALID_PARENT_EMAIL,
                VALID_APPOINTMENT_START,
                VALID_PAYMENT_DATES, VALID_PAYMENT_DUE_DATE,
                VALID_PAYMENT_RECURRENCE, VALID_TUITION_FEE,
                VALID_ATTENDANCE_HISTORY);

        assertEquals(2, person.toModelType().getAttendance().getRecords().size());
        assertEquals(LocalDate.parse("2026-01-29"),
                person.toModelType().getAttendance().getLastRecord().orElseThrow().getRecordedDate());
    }

    @Test
    public void toModelType_attendanceHistory_roundTripsSuccessfully() throws Exception {
        JsonAdaptedPerson person = makeJsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_TAGS, VALID_ACADEMICS,
                VALID_PARENT_NAME, VALID_PARENT_PHONE, VALID_PARENT_EMAIL,
                VALID_APPOINTMENT_START,
                VALID_PAYMENT_DATES, VALID_PAYMENT_DUE_DATE,
                VALID_PAYMENT_RECURRENCE, VALID_TUITION_FEE,
                VALID_ATTENDANCE_HISTORY);

        assertEquals(2, person.toModelType().getAttendance().getRecords().size());
        assertEquals(LocalDate.parse("2026-01-29"),
                person.toModelType().getAttendance().getLastRecord().orElseThrow().getRecordedDate());
    }

    @Test
    public void toModelType_invalidSubjects_throwsIllegalValueException() {

        List<JsonAdaptedSubject> invalidSubjects = BENSON.getAcademics().getSubjects().stream()
                .map(JsonAdaptedSubject::new)
                .collect(Collectors.toCollection(ArrayList::new));

        invalidSubjects.add(new JsonAdaptedSubject("Math", "@Invalid"));

        JsonAdaptedPerson person =
                makeJsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_TAGS, new JsonAdaptedAcademics(invalidSubjects),
                        VALID_PARENT_NAME, VALID_PARENT_PHONE, VALID_PARENT_EMAIL,
                        VALID_APPOINTMENT_START,
                        VALID_PAYMENT_DATES, VALID_PAYMENT_DUE_DATE,
                        VALID_PAYMENT_RECURRENCE, VALID_TUITION_FEE,
                        List.of(VALID_ATTENDANCE_ENTRY));

        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_invalidParentName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                makeJsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_TAGS, VALID_ACADEMICS,
                        INVALID_NAME, VALID_PARENT_PHONE, VALID_PARENT_EMAIL,
                        VALID_APPOINTMENT_START,
                        VALID_PAYMENT_DATES, VALID_PAYMENT_DUE_DATE,
                        VALID_PAYMENT_RECURRENCE, VALID_TUITION_FEE,
                        List.of(VALID_ATTENDANCE_ENTRY));
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidParentPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                makeJsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_TAGS, VALID_ACADEMICS,
                        VALID_PARENT_NAME, INVALID_PHONE, VALID_PARENT_EMAIL,
                        VALID_APPOINTMENT_START,
                        VALID_PAYMENT_DATES, VALID_PAYMENT_DUE_DATE,
                        VALID_PAYMENT_RECURRENCE, VALID_TUITION_FEE,
                        List.of(VALID_ATTENDANCE_ENTRY));
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidParentEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                makeJsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_TAGS, VALID_ACADEMICS,
                        VALID_PARENT_NAME, VALID_PARENT_PHONE, INVALID_EMAIL,
                        VALID_APPOINTMENT_START,
                        VALID_PAYMENT_DATES, VALID_PAYMENT_DUE_DATE,
                        VALID_PAYMENT_RECURRENCE, VALID_TUITION_FEE,
                        List.of(VALID_ATTENDANCE_ENTRY));
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullLevel_returnsSubject() throws Exception {
        JsonAdaptedSubject subject = new JsonAdaptedSubject("Math", null);

        Subject modelSubject = subject.toModelType();

        assertEquals(new Subject("Math", null), modelSubject);
    }

    @Test
    public void toModelType_emptySubjects_returnsPerson() throws Exception {
        Academics invalidAcademics = new Academics((new HashSet<Subject>()));

        JsonAdaptedPerson person =
                makeJsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_TAGS, new JsonAdaptedAcademics(invalidAcademics),
                        VALID_PARENT_NAME, VALID_PARENT_PHONE, VALID_PARENT_EMAIL,
                        VALID_APPOINTMENT_START,
                        VALID_PAYMENT_DATES, VALID_PAYMENT_DUE_DATE,
                        VALID_PAYMENT_RECURRENCE, VALID_TUITION_FEE,
                        List.of(VALID_ATTENDANCE_ENTRY));

        assertEquals(new JsonAdaptedPerson(person.toModelType()).toModelType(), person.toModelType());
    }
}
