package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;

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
 * Adds a payment record date for an existing student in the address book.
 */
public class AddPaymentCommand extends AddCommand {

    public static final String SUB_COMMAND_WORD = "payment";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + SUB_COMMAND_WORD
            + ": Records the day tuition fees were paid by the student identified by the index number used "
            + "in the displayed student list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_DATE + "DATE\n"
            + "Example: " + COMMAND_WORD + " " + SUB_COMMAND_WORD + " 1 "
            + PREFIX_DATE + "2026-01-13";

    public static final String MESSAGE_ADD_PAYMENT_SUCCESS =
            "Recorded tuition payment of $%1$.2f for %2$s on %3$s.";
    public static final String MESSAGE_PAYMENT_DUE_DATE_ADVANCED =
            "Billing due date advanced from %1$s to %2$s.";
    public static final String MESSAGE_PAYMENT_DUE_DATE_UNCHANGED =
            "Billing due date remains %1$s because the added date is not later than the latest recorded payment.";
    public static final String MESSAGE_PAYMENT_DATE_IS_PRESENT = "Payment date %1$s is already present for %2$s";

    private final Index index;
    private final LocalDate paymentDate;

    /**
     * @param index of the person in the filtered person list to update
     * @param paymentDate the payment date to record
     */
    public AddPaymentCommand(Index index, LocalDate paymentDate) {
        requireNonNull(index);
        requireNonNull(paymentDate);
        this.index = index;
        this.paymentDate = paymentDate;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Person personToEdit = IndexedPersonResolver.getTargetPerson(model, index);
        LocalDate previousDueDate = personToEdit.getBilling().getCurrentDueDate();

        Billing updatedBilling;
        try {
            updatedBilling = personToEdit.recordFeesPaidAndAdvanceBilling(paymentDate);
        } catch (IllegalArgumentException err) {
            String formattedDate = paymentDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
            throw new CommandException(String.format(MESSAGE_PAYMENT_DATE_IS_PRESENT,
                    formattedDate, Messages.format(personToEdit)));
        }

        Person editedPerson = new PersonBuilder(personToEdit)
                .withBilling(updatedBilling)
                .build();
        model.setPerson(personToEdit, editedPerson);

        String formattedDate = paymentDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        String paymentFeedback = String.format(MESSAGE_ADD_PAYMENT_SUCCESS,
                editedPerson.getBilling().getTuitionFee(),
                Messages.format(editedPerson),
            formattedDate);
        String dueDateFeedback;
        if (updatedBilling.getCurrentDueDate().isAfter(previousDueDate)) {
            dueDateFeedback = String.format(MESSAGE_PAYMENT_DUE_DATE_ADVANCED,
                previousDueDate,
                updatedBilling.getCurrentDueDate());
        } else {
            dueDateFeedback = String.format(MESSAGE_PAYMENT_DUE_DATE_UNCHANGED,
                updatedBilling.getCurrentDueDate());
        }

        return new CommandResult(paymentFeedback + " " + dueDateFeedback, editedPerson);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AddPaymentCommand)) {
            return false;
        }

        AddPaymentCommand otherCommand = (AddPaymentCommand) other;
        return index.equals(otherCommand.index)
            && paymentDate.equals(otherCommand.paymentDate);
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
