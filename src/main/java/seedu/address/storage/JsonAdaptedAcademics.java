package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.academic.Academics;
import seedu.address.model.academic.Subject;

/**
 * Jackson-friendly version of {@link Academics}.
 */
class JsonAdaptedAcademics {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Academics' %s field is missing!";

    private final List<JsonAdaptedSubject> subjects = new ArrayList<>();
    private final String description;

    /**
     * Constructs a {@code JsonAdaptedAcademics} with the given details.
     */
    @JsonCreator
    public JsonAdaptedAcademics(
            @JsonProperty("subjects") List<JsonAdaptedSubject> subjects,
            @JsonProperty("description") String description) {

        if (subjects != null) {
            this.subjects.addAll(subjects);
        }
        this.description = description;
    }

    /**
     * Converts a given {@code Academics} into this class for Jackson use.
     */
    public JsonAdaptedAcademics(Academics source) {
        subjects.addAll(source.getSubjects().stream()
                .map(JsonAdaptedSubject::new)
                .toList());
        description = source.getDescription().orElse(null);
    }

    /**
     * Converts a given {@code subjects} into this class for Jackson use.
     */
    public JsonAdaptedAcademics(List<JsonAdaptedSubject> subjects) {
        if (subjects != null) {
            this.subjects.addAll(subjects);
        }
        this.description = null;
    }

    /**
     * Converts this Jackson-friendly adapted academics object into the model's {@code Academics}.
     */
    public Academics toModelType() throws IllegalValueException {

        final List<Subject> modelSubjects = new ArrayList<>();
        for (JsonAdaptedSubject subject : subjects) {
            modelSubjects.add(subject.toModelType());
        }

        final Set<Subject> subjectSet = new HashSet<>(modelSubjects);

        return new Academics(subjectSet, Optional.ofNullable(description));
    }
}
