package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.billing.Billing;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;

/**
 * Deletes a recorded payment date for a student identified by index
 */
public class DeletePaymentCommand extends DeleteCommand {

    public static final String SUB_COMMAND_WORD = "payment";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + SUB_COMMAND_WORD
        + ": Deletes the specified payment date for the student identified by the "
        + "index number used in the displayed student list.\n"
        + "Parameters: INDEX (must be a positive integer) d/DATE\n"
        + "Example: " + COMMAND_WORD + " " + SUB_COMMAND_WORD + " 1 d/2026-01-13";

    public static final String MESSAGE_PAYMENT_DATE_NOT_FOUND = "Payment date %1$s is not recorded for %2$s";
    public static final String MESSAGE_DELETE_PAYMENT_SUCCESS = "Deleted payment date %1$s for %2$s";

    private final LocalDate paymentDate;

    /**
     * @param index of the person in the filtered person list whose payment date is to be deleted
     * @param paymentDate payment date to delete
     */
    public DeletePaymentCommand(Index index, LocalDate paymentDate) {
        super(index);
        requireNonNull(paymentDate);
        this.paymentDate = paymentDate;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Person personToEdit = getTargetPerson(model);

        Billing updatedBilling;
        try {
            updatedBilling = personToEdit.deleteRecordedPayment(paymentDate);
        } catch (IllegalArgumentException err) {
            String formattedDate = paymentDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
            throw new CommandException(String.format(MESSAGE_PAYMENT_DATE_NOT_FOUND,
                    formattedDate, Messages.format(personToEdit)));
        }

        Person editedPerson = new PersonBuilder(personToEdit)
                .withBilling(updatedBilling)
                .build();
        replacePerson(model, personToEdit, editedPerson);

        String formattedDate = paymentDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        return new CommandResult(String.format(MESSAGE_DELETE_PAYMENT_SUCCESS,
                formattedDate, Messages.format(editedPerson)), editedPerson);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeletePaymentCommand)) {
            return false;
        }

        DeletePaymentCommand otherDeleteCommand = (DeletePaymentCommand) other;
        return index.equals(otherDeleteCommand.index)
                && paymentDate.equals(otherDeleteCommand.paymentDate);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(index, paymentDate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("paymentDate", paymentDate)
                .toString();
    }
}
