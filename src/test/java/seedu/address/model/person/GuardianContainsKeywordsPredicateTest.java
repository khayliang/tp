package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.getPersonBuilder;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

public class GuardianContainsKeywordsPredicateTest {

    private final Person personWithGuardian = getPersonBuilder("Student One")
            .withGuardian(new Guardian(
                    new Name("Alice Tan"),
                    new Phone("91234567"),
                    new Email("alice@example.com")))
            .build();

    private final Person personWithoutGuardian = getPersonBuilder("Student Two")
            .build();

    @Test
    public void equals() {
        GuardianContainsKeywordsPredicate firstPredicate =
                new GuardianContainsKeywordsPredicate(List.of("Alice"), List.of("9123"), List.of("alice@"));
        GuardianContainsKeywordsPredicate secondPredicate =
                new GuardianContainsKeywordsPredicate(List.of("Bob"), List.of("9999"), List.of("bob@"));

        assertTrue(firstPredicate.equals(firstPredicate));
        assertTrue(firstPredicate.equals(
                new GuardianContainsKeywordsPredicate(List.of("Alice"), List.of("9123"), List.of("alice@"))));
        assertFalse(firstPredicate.equals(secondPredicate));
        assertFalse(firstPredicate.equals(null));
        assertFalse(firstPredicate.equals(1));
    }

    @Test
    public void test_noGuardian_returnsFalse() {
        GuardianContainsKeywordsPredicate predicate =
                new GuardianContainsKeywordsPredicate(
                        List.of("Alice"), Collections.emptyList(), Collections.emptyList());

        assertFalse(predicate.test(personWithoutGuardian));
    }

    @Test
    public void test_noFilters_returnsFalse() {
        GuardianContainsKeywordsPredicate predicate =
                new GuardianContainsKeywordsPredicate(Collections.emptyList(), Collections.emptyList(),
                        Collections.emptyList());

        assertFalse(predicate.test(personWithGuardian));
    }

    @Test
    public void test_namePhoneAndEmailMatches_returnsTrue() {
        GuardianContainsKeywordsPredicate namePredicate =
                new GuardianContainsKeywordsPredicate(
                        List.of("Alice"), Collections.emptyList(), Collections.emptyList());
        GuardianContainsKeywordsPredicate partialNamePredicate =
                new GuardianContainsKeywordsPredicate(
                        List.of("lic"), Collections.emptyList(), Collections.emptyList());
        GuardianContainsKeywordsPredicate phonePredicate =
                new GuardianContainsKeywordsPredicate(
                        Collections.emptyList(), List.of("1234"), Collections.emptyList());
        GuardianContainsKeywordsPredicate emailPredicate =
                new GuardianContainsKeywordsPredicate(Collections.emptyList(), Collections.emptyList(),
                        List.of("EXAMPLE.COM"));

        assertTrue(namePredicate.test(personWithGuardian));
        assertTrue(partialNamePredicate.test(personWithGuardian));
        assertTrue(phonePredicate.test(personWithGuardian));
        assertTrue(emailPredicate.test(personWithGuardian));
    }

    @Test
    public void test_multipleFields_anyMatchingFieldReturnsTrue() {
        GuardianContainsKeywordsPredicate nameThenPhonePredicate =
                new GuardianContainsKeywordsPredicate(List.of("Alice"), List.of("0000"), Collections.emptyList());
        GuardianContainsKeywordsPredicate emailAfterFailuresPredicate =
                new GuardianContainsKeywordsPredicate(List.of("Bob"), List.of("0000"), List.of("alice@"));

        assertTrue(nameThenPhonePredicate.test(personWithGuardian));
        assertTrue(emailAfterFailuresPredicate.test(personWithGuardian));
    }

    @Test
    public void test_allFieldsDoNotMatch_returnsFalse() {
        GuardianContainsKeywordsPredicate predicate =
                new GuardianContainsKeywordsPredicate(List.of("Bob"), List.of("0000"), List.of("bob@"));

        assertFalse(predicate.test(personWithGuardian));
    }

    @Test
    public void toStringMethod() {
        GuardianContainsKeywordsPredicate predicate =
                new GuardianContainsKeywordsPredicate(List.of("Alice"), List.of("9123"), List.of("alice@"));

        String expected = GuardianContainsKeywordsPredicate.class.getCanonicalName()
                + "{nameKeywords=[Alice], phoneKeywords=[9123], emailKeywords=[alice@]}";
        assertEquals(expected, predicate.toString());
    }
}
