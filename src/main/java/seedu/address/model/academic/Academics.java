package seedu.address.model.academic;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Represents a student's academic profile.
 *
 * An {@code Academics} object encapsulates a set of {@link Subject}s
 * and optional notes describing the student's academic progress or remarks.
 *
 * This abstraction allows future extensions such as grades, performance tracking,
 * or historical academic data without modifying the {@code Person} class.
 */
public class Academics {

    private final Set<Subject> subjects;
    private final Optional<String> notes;

    /**
     * Constructs an {@code Academics} object with subjects and optional notes.
     */
    public Academics(Set<Subject> subjects, Optional<String> notes) {
        requireNonNull(subjects);
        requireNonNull(notes);
        this.subjects = new HashSet<>(subjects);
        this.notes = notes;
    }

    /**
     * Constructs an {@code Academics} object with subjects and no notes.
     */
    public Academics(Set<Subject> subjects) {
        this(subjects, Optional.empty());
    }

    /**
     * Constructs an {@code Academics} object with empty subjects and no notes.
     */
    public Academics() {
        this(new HashSet<>(), Optional.empty());
    }

    public Set<Subject> getSubjects() {
        return Collections.unmodifiableSet(subjects);
    }

    public List<Subject> getSortedSubjects() {
        return subjects.stream()
                .sorted(Comparator.comparing(s -> s.getName().toLowerCase()))
                .toList();
    }

    public Optional<String> getNotes() {
        return notes;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof Academics
                && subjects.equals(((Academics) other).subjects)
                && notes.equals(((Academics) other).notes));
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjects, notes);
    }
}
