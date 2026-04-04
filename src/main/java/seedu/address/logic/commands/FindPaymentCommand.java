package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.billing.PaymentDueMonthPredicate;

/**
 * Finds and lists all persons in the address book whose billing payment due date
 * falls within the specified year-month (YYYY-MM).
 */
public class FindPaymentCommand extends FindCommand {

    public static final String SUB_COMMAND_WORD = "payment";

    public static final String MESSAGE_USAGE = FindCommand.COMMAND_WORD + " " + SUB_COMMAND_WORD
            + ": Finds all displayed students whose payment due date matches the specified year-month (YYYY-MM).\n"
            + "Parameters: d/YYYY-MM\n"
            + "Example: " + FindCommand.COMMAND_WORD + " " + SUB_COMMAND_WORD + " d/2026-03";

    private final PaymentDueMonthPredicate predicate;

    public FindPaymentCommand(PaymentDueMonthPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonListWithAnd(predicate);
        return new CommandResult(String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW,
                model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof FindPaymentCommand
                && predicate.equals(((FindPaymentCommand) other).predicate));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
