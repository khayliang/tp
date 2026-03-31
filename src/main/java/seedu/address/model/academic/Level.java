package seedu.address.model.academic;

import seedu.address.model.util.StringUtil;

/**
 * Represents the level of a Subject.
 */
public enum Level {
    BASIC,
    STRONG;

    @Override
    public String toString() {
        return StringUtil.toTitleCase(name());
    }
}
