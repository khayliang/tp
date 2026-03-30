package seedu.address.model.academic;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class SubjectContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        SubjectContainsKeywordsPredicate firstPredicate =
                new SubjectContainsKeywordsPredicate(Set.of(new Subject("Math", null)));
        SubjectContainsKeywordsPredicate secondPredicate =
                new SubjectContainsKeywordsPredicate(Set.of(new Subject("Science", null)));

        // same object -> true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> true
        SubjectContainsKeywordsPredicate firstPredicateCopy =
                new SubjectContainsKeywordsPredicate(Set.of(new Subject("Math", null)));
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // symmetric
        assertTrue(firstPredicateCopy.equals(firstPredicate));

        // different types -> false
        assertFalse(firstPredicate.equals(1));

        // null -> false
        assertFalse(firstPredicate.equals(null));

        // different keywords -> false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_personContainsMatchingSubject_returnsTrue() {
        Person person = new PersonBuilder()
                .withAcademics(new Academics(Set.of(
                        new Subject("Math", null),
                        new Subject("Science", null))))
                .build();

        // single match
        SubjectContainsKeywordsPredicate predicate =
                new SubjectContainsKeywordsPredicate(Set.of(new Subject("Math", null)));
        assertTrue(predicate.test(person));

        // multiple match (OR logic)
        SubjectContainsKeywordsPredicate predicateMultiple =
                new SubjectContainsKeywordsPredicate(
                        Set.of(new Subject("Math", null), new Subject("English", null)));
        assertTrue(predicateMultiple.test(person));
    }

    @Test
    public void test_personDoesNotContainMatchingSubject_returnsFalse() {
        Person person = new PersonBuilder()
                .withAcademics(new Academics(Set.of(
                        new Subject("Math", null),
                        new Subject("Science", null))))
                .build();

        SubjectContainsKeywordsPredicate predicate =
                new SubjectContainsKeywordsPredicate(Set.of(new Subject("English", null)));

        assertFalse(predicate.test(person));
    }

    @Test
    public void test_emptyKeywordSet_returnsFalse() {
        Person person = new PersonBuilder()
                .withAcademics(new Academics(Set.of(
                        new Subject("Math", null))))
                .build();

        SubjectContainsKeywordsPredicate predicate =
                new SubjectContainsKeywordsPredicate(Set.of());

        assertFalse(predicate.test(person));
    }

    @Test
    public void test_partialMatch_returnsTrue() {
        Person person = new PersonBuilder()
                .withAcademics(new Academics(Set.of(
                        new Subject("Mathematics", null))))
                .build();

        SubjectContainsKeywordsPredicate predicate =
                new SubjectContainsKeywordsPredicate(Set.of(new Subject("math", null)));

        assertTrue(predicate.test(person));
    }
}
