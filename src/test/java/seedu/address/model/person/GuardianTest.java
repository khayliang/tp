package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

public class GuardianTest {

    private static final Name VALID_NAME = new Name("John Doe");
    private static final Phone VALID_PHONE = new Phone("91234567");
    private static final Email VALID_EMAIL = new Email("john@example.com");

    @Test
    public void constructor_allFields_success() {
        Guardian guardian = new Guardian(VALID_NAME, VALID_PHONE, VALID_EMAIL);
        assertEquals(VALID_NAME, guardian.getName());
        assertEquals(Optional.of(VALID_PHONE), guardian.getPhone());
        assertEquals(Optional.of(VALID_EMAIL), guardian.getEmail());
    }

    @Test
    public void constructor_nullName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Guardian(null, VALID_PHONE, VALID_EMAIL));
    }

    @Test
    public void constructor_optionalFieldsNull_success() {
        Guardian guardian = new Guardian(VALID_NAME, null, null);
        assertEquals(VALID_NAME, guardian.getName());
        assertEquals(Optional.empty(), guardian.getPhone());
        assertEquals(Optional.empty(), guardian.getEmail());
    }

    @Test
    public void equals() {
        Guardian guardian = new Guardian(VALID_NAME, VALID_PHONE, VALID_EMAIL);

        // same values -> returns true
        assertTrue(guardian.equals(new Guardian(VALID_NAME, VALID_PHONE, VALID_EMAIL)));

        // same object -> returns true
        assertTrue(guardian.equals(guardian));

        // null -> returns false
        assertFalse(guardian.equals(null));

        // different type -> returns false
        assertFalse(guardian.equals(5));

        // different name -> returns false
        assertFalse(guardian.equals(new Guardian(new Name("Jane Doe"), VALID_PHONE, VALID_EMAIL)));

        // different phone -> returns false
        assertFalse(guardian.equals(new Guardian(VALID_NAME, new Phone("99999999"), VALID_EMAIL)));

        // different email -> returns false
        assertFalse(guardian.equals(new Guardian(VALID_NAME, VALID_PHONE, new Email("other@example.com"))));

        // guardians with only name equal to each other
        Guardian nameOnly1 = new Guardian(VALID_NAME, null, null);
        Guardian nameOnly2 = new Guardian(VALID_NAME, null, null);
        assertTrue(nameOnly1.equals(nameOnly2));
    }

    @Test
    public void hashCode_sameValues_sameHashCode() {
        Guardian guardian1 = new Guardian(VALID_NAME, VALID_PHONE, VALID_EMAIL);
        Guardian guardian2 = new Guardian(VALID_NAME, VALID_PHONE, VALID_EMAIL);
        assertEquals(guardian1.hashCode(), guardian2.hashCode());
    }

    @Test
    public void toStringMethod() {
        Guardian guardian = new Guardian(VALID_NAME, VALID_PHONE, VALID_EMAIL);
        String expected = Guardian.class.getCanonicalName() + "{name=" + VALID_NAME + ", phone=" + VALID_PHONE
                + ", email=" + VALID_EMAIL + "}";
        assertEquals(expected, guardian.toString());
    }
}
