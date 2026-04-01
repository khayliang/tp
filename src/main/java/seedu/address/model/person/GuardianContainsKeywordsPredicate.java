package seedu.address.model.person;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Guardian} matches the given name, phone, and/or email keywords.
 * Name matching is case-insensitive full-word; phone and email matching is case-insensitive substring.
 * Only fields with non-empty keyword lists are tested; all tested fields must match (AND logic).
 */
public class GuardianContainsKeywordsPredicate implements Predicate<Person> {

    private final List<String> nameKeywords;
    private final List<String> phoneKeywords;
    private final List<String> emailKeywords;

    /**
     * Constructs a {@code GuardianContainsKeywordsPredicate}.
     * Pass an empty list for any field that should not be filtered on.
     */
    public GuardianContainsKeywordsPredicate(List<String> nameKeywords,
                                             List<String> phoneKeywords,
                                             List<String> emailKeywords) {
        this.nameKeywords = nameKeywords;
        this.phoneKeywords = phoneKeywords;
        this.emailKeywords = emailKeywords;
    }

    @Override
    public boolean test(Person person) {
        Optional<Guardian> guardianOpt = person.getGuardian();
        if (guardianOpt.isEmpty()) {
            return false;
        }
        Guardian guardian = guardianOpt.get();

        if (!nameKeywords.isEmpty()) {
            boolean nameMatches = nameKeywords.stream()
                    .anyMatch(kw -> StringUtil.containsWordIgnoreCase(guardian.getName().fullName, kw));
            if (!nameMatches) {
                return false;
            }
        }

        if (!phoneKeywords.isEmpty()) {
            String phone = guardian.getPhone().map(p -> p.value).orElse("");
            boolean phoneMatches = phoneKeywords.stream()
                    .anyMatch(kw -> phone.toLowerCase().contains(kw.toLowerCase()));
            if (!phoneMatches) {
                return false;
            }
        }

        if (!emailKeywords.isEmpty()) {
            String email = guardian.getEmail().map(e -> e.value).orElse("");
            boolean emailMatches = emailKeywords.stream()
                    .anyMatch(kw -> email.toLowerCase().contains(kw.toLowerCase()));
            if (!emailMatches) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GuardianContainsKeywordsPredicate)) {
            return false;
        }
        GuardianContainsKeywordsPredicate o = (GuardianContainsKeywordsPredicate) other;
        return Objects.equals(nameKeywords, o.nameKeywords)
                && Objects.equals(phoneKeywords, o.phoneKeywords)
                && Objects.equals(emailKeywords, o.emailKeywords);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameKeywords, phoneKeywords, emailKeywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("nameKeywords", nameKeywords)
                .add("phoneKeywords", phoneKeywords)
                .add("emailKeywords", emailKeywords)
                .toString();
    }
}
