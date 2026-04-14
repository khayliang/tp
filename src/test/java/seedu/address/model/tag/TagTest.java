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
        assertEquals("close friend", tag.tagName);
    }

    @Test
    public void constructor_trimsWhitespace_success() {
        Tag tag = new Tag("   best buddy   ");
        assertEquals("best buddy", tag.tagName);
    }

    @Test
    public void constructor_preservesCase_success() {
        Tag tag = new Tag("mIxEd CaSe");
        assertEquals("mIxEd CaSe", tag.tagName);
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
