package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.academic.Academics;
import seedu.address.model.academic.Level;
import seedu.address.model.academic.Subject;

public class JsonAdaptedAcademicsTest {

    @Test
    public void toModelType_validDetails_returnsAcademics() throws Exception {
        JsonAdaptedAcademics adaptedAcademics = new JsonAdaptedAcademics(
                List.of(
                        new JsonAdaptedSubject("Math", "STRONG"),
                        new JsonAdaptedSubject("Science", "BASIC")),
                "Needs revision");

        Academics academics = adaptedAcademics.toModelType();

        Academics expected = new Academics(
                Set.of(new Subject("Math", Level.STRONG), new Subject("Science", Level.BASIC)),
                Optional.of("Needs revision"));
        assertEquals(expected, academics);
    }

    @Test
    public void toModelType_nullSubjectsAndNullDescription_returnsEmptyAcademics() throws Exception {
        JsonAdaptedAcademics adaptedAcademics = new JsonAdaptedAcademics(null, null);

        Academics academics = adaptedAcademics.toModelType();

        assertTrue(academics.getSubjects().isEmpty());
        assertTrue(academics.getDescription().isEmpty());
    }

    @Test
    public void toModelType_duplicateSubjects_deduplicatesSubjects() throws Exception {
        JsonAdaptedAcademics adaptedAcademics = new JsonAdaptedAcademics(
                List.of(
                        new JsonAdaptedSubject("Math", "STRONG"),
                        new JsonAdaptedSubject("Math", "STRONG")),
                null);

        Academics academics = adaptedAcademics.toModelType();

        assertEquals(Set.of(new Subject("Math", Level.STRONG)), academics.getSubjects());
    }

    @Test
    public void toModelType_invalidSubject_throwsIllegalValueException() {
        JsonAdaptedAcademics adaptedAcademics = new JsonAdaptedAcademics(
                List.of(new JsonAdaptedSubject("M@th", "STRONG")),
                null);

        assertThrows(IllegalValueException.class, adaptedAcademics::toModelType);
    }

    @Test
    public void constructor_roundTrip_returnsEquivalentAcademics() throws Exception {
        Academics source = new Academics(
                Set.of(new Subject("Math", Level.STRONG)),
                Optional.of("Progressing steadily"));

        JsonAdaptedAcademics adaptedAcademics = new JsonAdaptedAcademics(source);

        assertEquals(source, adaptedAcademics.toModelType());
    }
}
