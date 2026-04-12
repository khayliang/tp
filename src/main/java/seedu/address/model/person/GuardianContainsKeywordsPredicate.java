package seedu.address.model.person;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Guardian} matches the given name, phone, and/or email keywords.
 * Name matching, phone and email matching are case-insensitive substring.
 * A match in any supplied field is sufficient.
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
        boolean hasFilters = !nameKeywords.isEmpty() || !phoneKeywords.isEmpty() || !emailKeywords.isEmpty();
        boolean matchesAnyField = false;

        if (!nameKeywords.isEmpty()) {
            String name = guardian.getName().fullName.toLowerCase(Locale.ROOT);
            matchesAnyField = matchesAnyField || nameKeywords.stream()
                    .map(String::trim)
                    .filter(keyword -> !keyword.isEmpty())
                    .map(keyword -> keyword.toLowerCase(Locale.ROOT))
                    .anyMatch(name::contains);
        }

        if (!phoneKeywords.isEmpty()) {
            String phone = guardian.getPhone().map(p -> p.value).orElse("");
            String lowerCasePhone = phone.toLowerCase(Locale.ROOT);
            matchesAnyField = matchesAnyField || phoneKeywords.stream()
                    .map(String::trim)
                    .filter(keyword -> !keyword.isEmpty())
                    .map(keyword -> keyword.toLowerCase(Locale.ROOT))
                    .anyMatch(lowerCasePhone::contains);
        }

        if (!emailKeywords.isEmpty()) {
            String email = guardian.getEmail().map(e -> e.value).orElse("");
            String lowerCaseEmail = email.toLowerCase(Locale.ROOT);
            matchesAnyField = matchesAnyField || emailKeywords.stream()
                    .map(String::trim)
                    .filter(keyword -> !keyword.isEmpty())
                    .map(keyword -> keyword.toLowerCase(Locale.ROOT))
                    .anyMatch(lowerCaseEmail::contains);
        }

        return hasFilters && matchesAnyField;
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
