package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.subject.Level;
import seedu.address.model.subject.Subject;

public class JsonAdaptedSubjectTest {
    private static final String INVALID_SUBJECT = "M@th";
    private static final String INVALID_LEVEL = "super advanced";
    private static final String VALID_SUBJECT = "Math";
    private static final String VALID_LEVEL = "STRONG";

    @Test
    public void toModelType_validSubjectDetails_returnsSubject() throws Exception {
        JsonAdaptedSubject subject = new JsonAdaptedSubject(VALID_SUBJECT, VALID_LEVEL);
        assertEquals(new Subject(VALID_SUBJECT, Level.STRONG), subject.toModelType());
    }

    @Test
    public void toModelType_invalidSubjectName_throwsIllegalValueException() {
        JsonAdaptedSubject subject = new JsonAdaptedSubject(INVALID_SUBJECT, VALID_LEVEL);
        String expectedMessage = Subject.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, subject::toModelType);
    }

    @Test
    public void toModelType_nullSubjectName_throwsIllegalValueException() {
        JsonAdaptedSubject subject = new JsonAdaptedSubject(null, VALID_LEVEL);
        assertThrows(IllegalValueException.class, subject::toModelType);
    }

    @Test
    public void toModelType_invalidLevel_throwsIllegalValueException() {
        JsonAdaptedSubject subject = new JsonAdaptedSubject(VALID_SUBJECT, INVALID_LEVEL);
        assertThrows(IllegalValueException.class, subject::toModelType);
    }
}
