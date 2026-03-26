package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.billing.Billing;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;

/**
 * Edits the date tuition fees were paid by an existing person in the address book.
 */
public class EditPaymentCommand extends EditCommand {

    public static final String SUB_COMMAND_WORD = "payment";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + SUB_COMMAND_WORD
            + ": Records the day tuition fees were paid by the student identified by the index number used "
            + "in the displayed student list.\n"
            + "Optional amount field to update the current tuition fee rate.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_DATE + "DATE\n"
            + "Example: " + COMMAND_WORD + " " + SUB_COMMAND_WORD + " 1 "
            + PREFIX_DATE + "2026-01-13 "
            + PREFIX_AMOUNT + "25";

    public static final String DATE_NOT_PROVIDED = "Please enter a valid payment date!";
    public static final String MESSAGE_EDIT_PAYMENT_SUCCESS = "%1$s paid by %2$s on %3$s";

    private final LocalDate paymentDate;
    private final Optional<Double> tuitionFee;

    /**
     * @param index of the person in the filtered person list to edit
     * @param paymentDate the payment date to set
     */
    public EditPaymentCommand(Index index, LocalDate paymentDate, Optional<Double> tuitionFee) {
        super(index);
        requireNonNull(paymentDate);
        this.paymentDate = paymentDate;
        this.tuitionFee = tuitionFee;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Person personToEdit = getTargetPerson(model);
        Billing updatedBilling = tuitionFee.isPresent()
                ? personToEdit.recordFeesPaidAndAdvanceBilling(paymentDate).updateRate(tuitionFee.get())
                : personToEdit.recordFeesPaidAndAdvanceBilling(paymentDate);
        Person editedPerson = new PersonBuilder(personToEdit)
                .withBilling(updatedBilling)
                .build();

        replacePerson(model, personToEdit, editedPerson);
        String formattedDate = paymentDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        return new CommandResult(String.format(MESSAGE_EDIT_PAYMENT_SUCCESS,
                editedPerson.getBilling().getTuitionFee(),
                Messages.format(editedPerson),
                formattedDate),
                editedPerson);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof EditPaymentCommand)) {
            return false;
        }

        EditPaymentCommand otherCommand = (EditPaymentCommand) other;
        return index.equals(otherCommand.index)
                && paymentDate.equals(otherCommand.paymentDate)
                && tuitionFee.equals(otherCommand.tuitionFee);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("paymentDate", paymentDate)
                .add("amount", tuitionFee.isPresent() ? tuitionFee.get() : null)
                .toString();
    }
}
