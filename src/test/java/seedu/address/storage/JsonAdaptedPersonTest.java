package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
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
    private static final String INVALID_PAYMENT_DATE = "2026-13-40T25:00:00";
    private static final String INVALID_LAST_ATTENDANCE = "2026-01-29T33:00:00";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final String VALID_APPOINTMENT_START = "2026-01-13T08:00:00";
    private static final String VALID_PAYMENT_DATE = "2026-01-13";
    private static final String VALID_LAST_ATTENDANCE = "2026-01-29T08:00:00";
    private static final String VALID_PARENT_NAME = BENSON.getParentName().map(pn -> pn.fullName).orElse(null);
    private static final String VALID_PARENT_PHONE = BENSON.getParentPhone().map(pp -> pp.value).orElse(null);
    private static final String VALID_PARENT_EMAIL = BENSON.getParentEmail().map(pe -> pe.value).orElse(null);
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream().map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(INVALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_PARENT_NAME, VALID_PARENT_PHONE, VALID_PARENT_EMAIL,
                        VALID_TAGS, VALID_APPOINTMENT_START, VALID_PAYMENT_DATE, VALID_LAST_ATTENDANCE);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(null, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_PARENT_NAME, VALID_PARENT_PHONE, VALID_PARENT_EMAIL,
                        VALID_TAGS, VALID_APPOINTMENT_START, VALID_PAYMENT_DATE, VALID_LAST_ATTENDANCE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_PARENT_NAME, VALID_PARENT_PHONE, VALID_PARENT_EMAIL,
                        VALID_TAGS, VALID_APPOINTMENT_START, VALID_PAYMENT_DATE, VALID_LAST_ATTENDANCE);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, null, VALID_EMAIL, VALID_ADDRESS,
                        VALID_PARENT_NAME, VALID_PARENT_PHONE, VALID_PARENT_EMAIL,
                        VALID_TAGS, VALID_APPOINTMENT_START, VALID_PAYMENT_DATE, VALID_LAST_ATTENDANCE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, INVALID_EMAIL, VALID_ADDRESS,
                        VALID_PARENT_NAME, VALID_PARENT_PHONE, VALID_PARENT_EMAIL,
                        VALID_TAGS, VALID_APPOINTMENT_START, VALID_PAYMENT_DATE, VALID_LAST_ATTENDANCE);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, null, VALID_ADDRESS,
                        VALID_PARENT_NAME, VALID_PARENT_PHONE, VALID_PARENT_EMAIL,
                        VALID_TAGS, VALID_APPOINTMENT_START, VALID_PAYMENT_DATE, VALID_LAST_ATTENDANCE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, INVALID_ADDRESS,
                        VALID_PARENT_NAME, VALID_PARENT_PHONE, VALID_PARENT_EMAIL,
                        VALID_TAGS, VALID_APPOINTMENT_START, VALID_PAYMENT_DATE, VALID_LAST_ATTENDANCE);
        String expectedMessage = Address.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, null,
                        VALID_PARENT_NAME, VALID_PARENT_PHONE, VALID_PARENT_EMAIL,
                        VALID_TAGS, VALID_APPOINTMENT_START, VALID_PAYMENT_DATE, VALID_LAST_ATTENDANCE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_PARENT_NAME, VALID_PARENT_PHONE, VALID_PARENT_EMAIL,
                        invalidTags, VALID_APPOINTMENT_START, VALID_PAYMENT_DATE, VALID_LAST_ATTENDANCE);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_invalidAppointmentStart_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_PARENT_NAME, VALID_PARENT_PHONE, VALID_PARENT_EMAIL,
                        VALID_TAGS, INVALID_APPOINTMENT_START, VALID_PAYMENT_DATE, VALID_LAST_ATTENDANCE);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_invalidPaymentDate_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_PARENT_NAME, VALID_PARENT_PHONE, VALID_PARENT_EMAIL,
                        VALID_TAGS, VALID_APPOINTMENT_START, INVALID_PAYMENT_DATE, VALID_LAST_ATTENDANCE);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_invalidLastAttendance_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                        VALID_PARENT_NAME, VALID_PARENT_PHONE, VALID_PARENT_EMAIL,
                        VALID_TAGS, VALID_APPOINTMENT_START, VALID_PAYMENT_DATE, INVALID_LAST_ATTENDANCE);
        assertThrows(IllegalValueException.class, person::toModelType);
    }

}
