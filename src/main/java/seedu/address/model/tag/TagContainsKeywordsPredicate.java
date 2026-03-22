package seedu.address.model.tag;

import java.util.Set;
import java.util.function.Predicate;

import seedu.address.model.person.Person;

/**
 * Tests whether a {@code Person}'s tags contain any of the given keywords.
 * <p>
 * Matching is case-insensitive and based on substring (partial) matching.
 * A person will be matched if at least one of their tags contains
 * any of the specified keywords.
 */
public class TagContainsKeywordsPredicate implements Predicate<Person> {

    private final Set<Tag> keywords;

    /**
     * Constructs a predicate with the specified set of tag keywords.
     *
     * @param keywords Set of tags to match against.
     */
    public TagContainsKeywordsPredicate(Set<Tag> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        return person.getTags().stream()
                .anyMatch(personTag ->
                        keywords.stream().anyMatch(keyword ->
                                personTag.tagName.toLowerCase()
                                        .contains(keyword.tagName.toLowerCase())));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof TagContainsKeywordsPredicate
                && keywords.equals(((TagContainsKeywordsPredicate) other).keywords));
    }
}
