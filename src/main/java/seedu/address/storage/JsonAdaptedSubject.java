package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.subject.Level;
import seedu.address.model.subject.Subject;

/**
 * Jackson-friendly version of {@link Subject}.
 */
class JsonAdaptedSubject {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Subject's %s field is missing!";

    private final String name;
    private final String level; // optional

    /**
     * Constructs a {@code JsonAdaptedSubject} with the given details.
     */
    @JsonCreator
    public JsonAdaptedSubject(@JsonProperty("name") String name,
                              @JsonProperty("level") String level) {
        this.name = name;
        this.level = level;
    }

    /**
     * Converts a given {@code Subject} into this class for Jackson use.
     */
    public JsonAdaptedSubject(Subject source) {
        name = source.getName();
        level = source.getLevel().map(Level::toString).orElse(null);
    }

    /**
     * Converts this Jackson-friendly adapted subject object into the model's {@code Subject}.
     */
    public Subject toModelType() throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, "name"));
        }
        if (!Subject.isValidSubjectName(name)) {
            throw new IllegalValueException(Subject.MESSAGE_CONSTRAINTS);
        }

        Level modelLevel = null;
        if (level != null) {
            try {
                modelLevel = Level.fromString(level);
            } catch (IllegalArgumentException e) {
                throw new IllegalValueException(Level.MESSAGE_CONSTRAINTS);
            }
        }

        return new Subject(name, modelLevel);
    }
}
