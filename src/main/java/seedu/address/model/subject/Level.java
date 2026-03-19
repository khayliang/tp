package seedu.address.model.subject;

/**
 * Represents the level of a Subject.
 */
public enum Level {
    BASIC,
    STRONG;

    public static final String MESSAGE_CONSTRAINTS =
            "Level must be either 'basic' or 'strong' (case-insensitive).";

    /**
     * Parses a string into a Level.
     */
    public static Level fromString(String input) {
        if (input == null) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }

        switch (input.toLowerCase()) {
        case "basic":
            return BASIC;
        case "strong":
            return STRONG;
        default:
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
    }

    @Override
    public String toString() {
        return name().toLowerCase(); // display as "basic", "strong"
    }
}
