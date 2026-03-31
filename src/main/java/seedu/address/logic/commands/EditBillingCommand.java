package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AMOUNT;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.billing.Billing;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;

/**
 * Edits billing fields of an existing person in the address book.
 */
public class EditBillingCommand extends EditCommand {

    public static final String SUB_COMMAND_WORD = "billing";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + SUB_COMMAND_WORD
            + ": Updates billing configuration for the student identified by the index number used "
            + "in the displayed student list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_AMOUNT + "AMOUNT\n"
            + "Example: " + COMMAND_WORD + " " + SUB_COMMAND_WORD + " 1 "
            + PREFIX_AMOUNT + "50";

    public static final String MESSAGE_EDIT_BILLING_SUCCESS = "Updated tuition fee for %1$s: $%2$.2f";

    private final double tuitionFee;

    /**
     * @param index of the person in the filtered person list to edit
     * @param tuitionFee the tuition fee to set
     */
    public EditBillingCommand(Index index, double tuitionFee) {
        super(index);
        this.tuitionFee = tuitionFee;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Person personToEdit = getTargetPerson(model);
        Billing updatedBilling = personToEdit.updateTuitionRate(tuitionFee);
        Person editedPerson = new PersonBuilder(personToEdit)
                .withBilling(updatedBilling)
                .build();

        replacePerson(model, personToEdit, editedPerson);

        return new CommandResult(String.format(MESSAGE_EDIT_BILLING_SUCCESS,
                Messages.format(editedPerson), updatedBilling.getTuitionFee()), editedPerson);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof EditBillingCommand)) {
            return false;
        }

        EditBillingCommand otherCommand = (EditBillingCommand) other;
        return index.equals(otherCommand.index)
                && Double.compare(tuitionFee, otherCommand.tuitionFee) == 0;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("tuitionFee", tuitionFee)
                .toString();
    }
}
