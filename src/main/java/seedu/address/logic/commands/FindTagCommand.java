package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.ListDisplayMode;
import seedu.address.model.Model;
import seedu.address.model.tag.TagContainsKeywordsPredicate;

/**
 * Finds and lists all persons in the address book whose tags contain any of the specified keywords.
 * <p>
 * Keyword matching is case-insensitive and supports partial matching.
 * For example, the keyword "math" will match tags such as "Math" or "Mathematics".
 * <p>
 * A person is considered a match if at least one of their tags contains
 * any of the given keywords.
 */
public class FindTagCommand extends FindCommand {

    public static final String SUB_COMMAND_WORD = "tag";

    public static final String MESSAGE_USAGE = FindCommand.COMMAND_WORD + " " + SUB_COMMAND_WORD
            + ": Finds all persons whose tags contain any of the specified keywords "
            + "(case-insensitive, partial match).\n"
            + "Parameters: t/TAG [t/MORE_TAGS]...\n"
            + "Example: " + FindCommand.COMMAND_WORD + " " + SUB_COMMAND_WORD + " t/JC t/Sec1";

    private final TagContainsKeywordsPredicate predicate;

    /**
     * Creates a {@code FindTagCommand} with the given predicate.
     *
     * @param predicate Predicate used to filter persons based on tag keywords.
     */
    public FindTagCommand(TagContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        model.setListDisplayMode(ListDisplayMode.PERSON); // same as FindPerson
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW,
                        model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof FindTagCommand
                && predicate.equals(((FindTagCommand) other).predicate));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
