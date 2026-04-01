package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.ListDisplayMode;
import seedu.address.model.Model;
import seedu.address.model.person.GuardianContainsKeywordsPredicate;

/**
 * Finds and lists all students in the address book whose guardian's name, phone, or email
 * contains any of the argument keywords. Keyword matching is case-insensitive.
 */
public class FindParentCommand extends FindCommand {

    public static final String SUB_COMMAND_WORD = "parent";

    public static final String MESSAGE_USAGE = FindCommand.COMMAND_WORD + " " + SUB_COMMAND_WORD
            + ": Finds all students whose parent/guardian details contain any of the specified keywords "
            + "(case-insensitive). At least one prefix must be provided.\n"
            + "Parameters: [n/NAME_KEYWORD]... [p/PHONE_KEYWORD]... [e/EMAIL_KEYWORD]...\n"
            + "Example: " + FindCommand.COMMAND_WORD + " " + SUB_COMMAND_WORD + " n/Alice p/91234567";

    private final GuardianContainsKeywordsPredicate predicate;

    public FindParentCommand(GuardianContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonListWithAnd(predicate);
        model.setListDisplayMode(ListDisplayMode.PERSON);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof FindParentCommand)) {
            return false;
        }
        FindParentCommand otherFindParentCommand = (FindParentCommand) other;
        return predicate.equals(otherFindParentCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
