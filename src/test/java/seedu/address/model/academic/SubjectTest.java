package seedu.address.model.academic;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class SubjectTest {

    @Test
    public void constructor_nullName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Subject(null, null));
    }

    @Test
    public void constructor_invalidSubjectName_throwsIllegalArgumentException() {
        String invalidName = ""; // invalid like Tag
        assertThrows(IllegalArgumentException.class, () -> new Subject(invalidName, null));
    }

    @Test
    public void isValidSubjectName() {
        // null subject name
        assertThrows(NullPointerException.class, () -> Subject.isValidSubjectName(null));
    }

    @Test
    public void constructor_validNameNoLevel_success() {
        assertDoesNotThrow(() -> new Subject("Math", null));
    }

    @Test
    public void constructor_validNameWithLevel_success() {
        assertDoesNotThrow(() -> new Subject("Math", Level.STRONG));
    }

    @Test
    public void toString_noLevel_returnsNameOnly() {
        Subject subject = new Subject("Math", null);
        assert(subject.toString().equals("Math"));
    }

    @Test
    public void toString_withLevel_returnsFormatted() {
        Subject subject = new Subject("Math", Level.STRONG);
        assert(subject.toString().equals("Math-strong"));
    }

    @Test
    public void equals() {
        Subject s1 = new Subject("Math", Level.STRONG);
        Subject s2 = new Subject("Math", Level.STRONG);
        Subject s3 = new Subject("Math", null);

        // same values
        assert(s1.equals(s2));

        // different level
        assert(!s1.equals(s3));

        // same object
        assert(s1.equals(s1));

        // null
        assert(!s1.equals(null));

        // different type
        assert(!s1.equals(5));
    }

    @Test
    public void equals_nullLevelHandledCorrectly() {
        Subject s1 = new Subject("Math", null);
        Subject s2 = new Subject("Math", null);

        assert(s1.equals(s2));
    }
}
