package seedu.address.model.academic;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class SubjectTest {

    @Test
    public void constructor_nullName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Subject(null, null));
    }

    @Test
    public void constructor_invalidSubjectName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Subject("", null));
        assertThrows(IllegalArgumentException.class, () -> new Subject("   ", null));
        assertThrows(IllegalArgumentException.class, () -> new Subject("!!!", null));
    }

    @Test
    public void constructor_validNameWithSpaces_success() {
        Subject subject = new Subject("data structures", null);
        assertEquals("data structures", subject.getName());
    }

    @Test
    public void constructor_trimsWhitespace_success() {
        Subject subject = new Subject("   discrete math   ", null);
        assertEquals("discrete math", subject.getName());
    }

    @Test
    public void constructor_preservesCase_success() {
        Subject subject = new Subject("mIxEd CaSe", null);
        assertEquals("mIxEd CaSe", subject.getName());
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
        Subject subject = new Subject("math", null);
        assertEquals("math", subject.toString());
    }

    @Test
    public void toString_withLevel_returnsFormatted() {
        Subject subject = new Subject("math", Level.STRONG);
        assertEquals("math-Strong", subject.toString());
    }

    @Test
    public void equals_sameValues_returnsTrue() {
        Subject s1 = new Subject("math", Level.STRONG);
        Subject s2 = new Subject("Math", Level.STRONG);

        // equality ignores case
        assert(s1.equals(s2));
    }

    @Test
    public void equals_differentLevel_returnsFalse() {
        Subject s1 = new Subject("Math", Level.STRONG);
        Subject s2 = new Subject("Math", null);

        assert(!s1.equals(s2));
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        Subject s1 = new Subject("Math", null);
        assert(s1.equals(s1));
    }

    @Test
    public void equals_null_returnsFalse() {
        Subject s1 = new Subject("Math", null);
        assert(!s1.equals(null));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        Subject s1 = new Subject("Math", null);
        assert(!s1.equals(5));
    }

    @Test
    public void equals_nullLevelHandledCorrectly() {
        Subject s1 = new Subject("Math", null);
        Subject s2 = new Subject("math", null);

        assert(s1.equals(s2));
    }

    @Test
    public void isValidSubjectName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> Subject.isValidSubjectName(null));
    }

    @Test
    public void isValidSubjectName_validInputs_returnsTrue() {
        assert(Subject.isValidSubjectName("Math"));
        assert(Subject.isValidSubjectName("Data Structures"));
    }

    @Test
    public void isValidSubjectName_invalidInputs_returnsFalse() {
        assert(!Subject.isValidSubjectName(""));
        assert(!Subject.isValidSubjectName("   "));
        assert(!Subject.isValidSubjectName("!!!"));
    }
}
