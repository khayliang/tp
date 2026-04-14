package seedu.address.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Tag in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTagName(String)}
 */
public class Tag {

    public static final String MESSAGE_CONSTRAINTS =
            "Tag names should be alphanumeric and may contain spaces between words";
    public static final String VALIDATION_REGEX = "[\\p{Alnum}]+( [\\p{Alnum}]+)*";

    public final String tagName;

    /**
     * Constructs a {@code Tag}.
     *
     * @param tagName A valid tag name.
     */
    public Tag(String tagName) {
        requireNonNull(tagName);

        String trimmed = tagName.trim();
        checkArgument(!trimmed.isEmpty(), MESSAGE_CONSTRAINTS);

        checkArgument(isValidTagName(trimmed), MESSAGE_CONSTRAINTS);

        this.tagName = trimmed;
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidTagName(String test) {
        requireNonNull(test);

        String trimmed = test.trim();
        if (trimmed.isEmpty()) {
            return false;
        }

        String normalized = trimmed.toLowerCase();
        return normalized.matches(VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Tag)) {
            return false;
        }

        Tag otherTag = (Tag) other;
        return tagName.equalsIgnoreCase(otherTag.tagName);
    }

    @Override
    public int hashCode() {
        return tagName.toLowerCase().hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    @Override
    public String toString() {
        return tagName;
    }

}
