package seedu.address.model.academic;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import seedu.address.model.person.Person;

/**
 * Tests whether a {@code Person}'s subjects contain any of the given keywords.
 * <p>
 * Matching is case-insensitive and based on substring (partial) matching.
 * A person will be matched if at least one of their subjects contains
 * any of the specified keywords.
 */
public class SubjectContainsKeywordsPredicate implements Predicate<Person> {

    private final Set<Subject> keywords;
    private final Set<String> keywordNames;

    /**
     * Constructs a predicate with the specified set of subject keywords.
     *
     * @param keywords Set of subjects to match against.
     */
    public SubjectContainsKeywordsPredicate(Set<Subject> keywords) {
        this.keywords = keywords;
        this.keywordNames = keywords.stream()
                .map(subject -> subject.getName().toLowerCase())
                .collect(Collectors.toSet());
    }

    @Override
    public boolean test(Person person) {
        return person.getAcademics().getSubjects().stream()
                .map(subject -> subject.getName().toLowerCase())
                .anyMatch(subjectName ->
                        keywordNames.stream().anyMatch(subjectName::contains));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof SubjectContainsKeywordsPredicate
                && keywords.equals(((SubjectContainsKeywordsPredicate) other).keywords));
    }
}
