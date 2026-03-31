package seedu.address.model.academic;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Objects;
import java.util.Optional;

import seedu.address.model.util.StringUtil;

/**
 * Represents a Subject with an optional level.
 * Guarantees: immutable; name is valid; level is optional.
 */
public class Subject {

    public static final String MESSAGE_CONSTRAINTS =
            "Subject names should be alphanumeric and may contain spaces between words";

    public static final String VALIDATION_REGEX = "[\\p{Alnum}]+( [\\p{Alnum}]+)*";

    private final String name;
    private final Level level; // nullable

    /**
     * Constructs a {@code Subject}.
     *
     * @param name A valid subject name.
     * @param level Optional level (can be null).
     */
    public Subject(String name, Level level) {
        requireNonNull(name);

        String trimmed = name.trim();
        checkArgument(!trimmed.isEmpty(), MESSAGE_CONSTRAINTS);

        String normalized = StringUtil.toTitleCase(trimmed);
        checkArgument(isValidSubjectName(normalized), MESSAGE_CONSTRAINTS);

        this.name = normalized;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public Optional<Level> getLevel() {
        return Optional.ofNullable(level);
    }

    /**
     * Returns true if a given string is a valid subject name.
     */
    public static boolean isValidSubjectName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns a new Subject with updated level.
     */
    public Subject withLevel(Level newLevel) {
        return new Subject(this.name, newLevel);
    }

    @Override
    public String toString() {
        return level == null
                ? name
                : name + "-" + level.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Subject)) {
            return false;
        }

        Subject otherSubject = (Subject) other;
        return name.equals(otherSubject.name)
                && Objects.equals(level, otherSubject.level);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, level);
    }
}
