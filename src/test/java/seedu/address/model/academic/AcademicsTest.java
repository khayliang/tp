package seedu.address.model.academic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class AcademicsTest {

    @Test
    public void constructor_nullSubjects_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Academics(null, Optional.empty()));
    }

    @Test
    public void constructor_nullDescription_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Academics(new HashSet<>(), null));
    }

    @Test
    public void constructor_validInputs_success() {
        Set<Subject> subjects = new HashSet<>();
        subjects.add(new Subject("Math", null));

        Academics academics = new Academics(subjects, Optional.of("Good progress"));

        assertEquals(1, academics.getSubjects().size());
        assertTrue(academics.getDescription().isPresent());
        assertEquals("Good progress", academics.getDescription().get());
    }

    @Test
    public void constructor_subjectsOnly_descriptionEmpty() {
        Set<Subject> subjects = new HashSet<>();
        subjects.add(new Subject("Math", null));

        Academics academics = new Academics(subjects);

        assertEquals(1, academics.getSubjects().size());
        assertTrue(academics.getDescription().isEmpty());
    }

    @Test
    public void constructor_default_emptyFields() {
        Academics academics = new Academics();

        assertTrue(academics.getSubjects().isEmpty());
        assertTrue(academics.getDescription().isEmpty());
    }

    @Test
    public void getSubjects_returnsUnmodifiableSet() {
        Set<Subject> subjects = new HashSet<>();
        subjects.add(new Subject("Math", null));

        Academics academics = new Academics(subjects);

        Set<Subject> returned = academics.getSubjects();

        assertThrows(
                UnsupportedOperationException.class, () -> returned.add(new Subject("English", null))
        );
    }

    @Test
    public void constructor_defensiveCopy_subjectsNotAffected() {
        Set<Subject> subjects = new HashSet<>();
        subjects.add(new Subject("Math", null));

        Academics academics = new Academics(subjects);

        // modify original set
        subjects.add(new Subject("English", null));

        // academics should NOT change
        assertEquals(1, academics.getSubjects().size());
    }

    @Test
    public void equals() {
        Set<Subject> subjects1 = new HashSet<>();
        subjects1.add(new Subject("Math", null));

        Set<Subject> subjects2 = new HashSet<>();
        subjects2.add(new Subject("Math", null));

        Academics a1 = new Academics(subjects1, Optional.of("Note"));
        Academics a2 = new Academics(subjects2, Optional.of("Note"));
        Academics a3 = new Academics(subjects2, Optional.empty());

        assertEquals(a1, a2);
        assertEquals(a1, a1);
        assertNotEquals(a1, null);
        assertNotEquals(a1, 5);
        assertNotEquals(a1, a3);
    }

    @Test
    public void hashCode_consistency() {
        Set<Subject> subjects1 = new HashSet<>();
        subjects1.add(new Subject("Math", null));

        Set<Subject> subjects2 = new HashSet<>();
        subjects2.add(new Subject("Math", null));

        Academics a1 = new Academics(subjects1, Optional.of("Note"));
        Academics a2 = new Academics(subjects2, Optional.of("Note"));

        assertEquals(a1.hashCode(), a2.hashCode());
    }
}
