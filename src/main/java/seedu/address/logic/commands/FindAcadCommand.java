package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.academic.SubjectContainsKeywordsPredicate;

/**
 * Finds and lists all persons in the address book whose subjects contain any of the specified keywords.
 * <p>
 * Keyword matching is case-insensitive and supports partial matching.
 * For example, the keyword "math" will match subjects such as "Math" or "Mathematics".
 * <p>
 * A person is considered a match if at least one of their subjects contains
 * any of the given keywords.
 */
public class FindAcadCommand extends FindCommand {

    public static final String SUB_COMMAND_WORD = "acad";

    public static final String MESSAGE_USAGE = FindCommand.COMMAND_WORD + " " + SUB_COMMAND_WORD
            + ": Finds all persons whose subjects contain any of the specified keywords "
            + "(case-insensitive, partial match).\n"
            + "Parameters: s/SUBJECT [s/MORE_SUBJECTS]...\n"
            + "Example: " + FindCommand.COMMAND_WORD + " " + SUB_COMMAND_WORD + " s/Math s/Science";

    private final SubjectContainsKeywordsPredicate predicate;

    /**
     * Creates a {@code FindAcadCommand} with the given predicate.
     *
     * @param predicate Predicate used to filter persons based on subject keywords.
     */
    public FindAcadCommand(SubjectContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonListWithAnd(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW,
                        model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof FindAcadCommand
                && predicate.equals(((FindAcadCommand) other).predicate));
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
