package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class TagContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        TagContainsKeywordsPredicate firstPredicate =
                new TagContainsKeywordsPredicate(Set.of(new Tag("JC")));
        TagContainsKeywordsPredicate secondPredicate =
                new TagContainsKeywordsPredicate(Set.of(new Tag("P6")));

        // same object -> true (covers other == this)
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> true
        TagContainsKeywordsPredicate firstPredicateCopy =
                new TagContainsKeywordsPredicate(Set.of(new Tag("JC")));
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
    public void test_personContainsMatchingTag_returnsTrue() {
        Person person = new PersonBuilder()
                .withTags("JC", "P6")
                .build();

        // single match
        TagContainsKeywordsPredicate predicate =
                new TagContainsKeywordsPredicate(Set.of(new Tag("JC")));
        assertTrue(predicate.test(person));

        // multiple match (OR logic)
        TagContainsKeywordsPredicate predicateMultiple =
                new TagContainsKeywordsPredicate(
                        Set.of(new Tag("JC"), new Tag("SEC2")));
        assertTrue(predicateMultiple.test(person));
    }

    @Test
    public void test_personDoesNotContainMatchingTag_returnsFalse() {
        Person person = new PersonBuilder()
                .withTags("JC", "P6")
                .build();

        TagContainsKeywordsPredicate predicate =
                new TagContainsKeywordsPredicate(Set.of(new Tag("SEC2")));

        assertFalse(predicate.test(person));
    }

    @Test
    public void test_emptyKeywordSet_returnsFalse() {
        Person person = new PersonBuilder()
                .withTags("JC")
                .build();

        TagContainsKeywordsPredicate predicate =
                new TagContainsKeywordsPredicate(Set.of());

        assertFalse(predicate.test(person));
    }
}
