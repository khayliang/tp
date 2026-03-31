package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class StringUtilTest {

    @Test
    public void toTitleCase_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> StringUtil.toTitleCase(null));
    }

    @Test
    public void toTitleCase_emptyString_returnsEmptyString() {
        assertEquals("", StringUtil.toTitleCase(""));
    }

    @Test
    public void toTitleCaseWhitespaceOnlyReturnsEmptyString() {
        assertEquals("", StringUtil.toTitleCase("     "));
    }

    @Test
    public void toTitleCaseSingleWordLowercaseConverted() {
        assertEquals("Hello", StringUtil.toTitleCase("hello"));
    }

    @Test
    public void toTitleCaseSingleWordUppercaseConverted() {
        assertEquals("Hello", StringUtil.toTitleCase("HELLO"));
    }

    @Test
    public void toTitleCaseSingleWordMixedCaseConverted() {
        assertEquals("Hello", StringUtil.toTitleCase("hElLo"));
    }

    @Test
    public void toTitleCaseMultipleWordsConverted() {
        assertEquals("Hello World", StringUtil.toTitleCase("hello world"));
    }

    @Test
    public void toTitleCaseMultipleSpacesNormalized() {
        assertEquals("Hello World", StringUtil.toTitleCase("hello   world"));
    }

    @Test
    public void toTitleCaseLeadingTrailingSpacesTrimmed() {
        assertEquals("Hello World", StringUtil.toTitleCase("   hello world   "));
    }

    @Test
    public void toTitleCaseMixedCaseSentenceNormalized() {
        assertEquals("Data Structures And Algorithms",
                StringUtil.toTitleCase("dAtA sTrUcTuReS AND aLgOrItHmS"));
    }

    @Test
    public void toTitleCaseAlphanumericWordsPreserved() {
        assertEquals("Cs2103 Project", StringUtil.toTitleCase("cs2103 project"));
    }

    @Test
    public void toTitleCaseMultipleSpacesBetweenWordsNormalizedToSingleSpace() {
        assertEquals("Discrete Math", StringUtil.toTitleCase("Discrete    Math"));
    }
}
