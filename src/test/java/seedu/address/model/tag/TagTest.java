package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Tag(""));
        assertThrows(IllegalArgumentException.class, () -> new Tag("   "));
        assertThrows(IllegalArgumentException.class, () -> new Tag("!!!"));
    }

    @Test
    public void constructor_validTagNameWithSpaces_success() {
        Tag tag = new Tag("close friend");
        assertEquals("Close Friend", tag.tagName);
    }

    @Test
    public void constructor_trimsWhitespace_success() {
        Tag tag = new Tag("   best buddy   ");
        assertEquals("Best Buddy", tag.tagName);
    }

    @Test
    public void constructor_normalizesCase_success() {
        Tag tag = new Tag("mIxEd CaSe");
        assertEquals("Mixed Case", tag.tagName);
    }

    @Test
    public void isValidTagName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> Tag.isValidTagName(null));
    }

    @Test
    public void isValidTagName_validInputs_returnsTrue() {
        assert(Tag.isValidTagName("Math"));
        assert(Tag.isValidTagName("Data Structures"));
    }

    @Test
    public void isValidTagName_invalidInputs_returnsFalse() {
        assert(!Tag.isValidTagName(""));
        assert(!Tag.isValidTagName("   "));
        assert(!Tag.isValidTagName("!!!"));
    }
}
