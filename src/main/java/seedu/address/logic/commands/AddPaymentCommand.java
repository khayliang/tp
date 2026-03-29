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
 * Adds a payment record date for an existing student in the address book.
 */
public class AddPaymentCommand extends AddCommand {

    public static final String SUB_COMMAND_WORD = "payment";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + SUB_COMMAND_WORD
            + ": Records the day tuition fees were paid by the student identified by the index number used "
            + "in the displayed student list.\n"
            + "Optional amount field to update the current tuition fee rate.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_DATE + "DATE [" + PREFIX_AMOUNT + "AMOUNT]\n"
            + "Example: " + COMMAND_WORD + " " + SUB_COMMAND_WORD + " 1 "
            + PREFIX_DATE + "2026-01-13 "
            + PREFIX_AMOUNT + "25";

    public static final String MESSAGE_ADD_PAYMENT_SUCCESS = "%1$s paid by %2$s on %3$s";

    private final Index index;
    private final LocalDate paymentDate;
    private final Optional<Double> tuitionFee;

    /**
     * @param index of the person in the filtered person list to update
     * @param paymentDate the payment date to record
     * @param tuitionFee optional tuition fee update amount
     */
    public AddPaymentCommand(Index index, LocalDate paymentDate, Optional<Double> tuitionFee) {
        requireNonNull(index);
        requireNonNull(paymentDate);
        requireNonNull(tuitionFee);
        this.index = index;
        this.paymentDate = paymentDate;
        this.tuitionFee = tuitionFee;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Person personToEdit = IndexedPersonResolver.getTargetPerson(model, index);
        Billing updatedBilling = tuitionFee.isPresent()
                ? personToEdit.recordFeesPaidAndAdvanceBilling(paymentDate).updateRate(tuitionFee.get())
                : personToEdit.recordFeesPaidAndAdvanceBilling(paymentDate);

        Person editedPerson = new PersonBuilder(personToEdit)
                .withBilling(updatedBilling)
                .build();
        model.setPerson(personToEdit, editedPerson);

        String formattedDate = paymentDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        return new CommandResult(String.format(MESSAGE_ADD_PAYMENT_SUCCESS,
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

        if (!(other instanceof AddPaymentCommand)) {
            return false;
        }

        AddPaymentCommand otherCommand = (AddPaymentCommand) other;
        return index.equals(otherCommand.index)
                && paymentDate.equals(otherCommand.paymentDate)
                && tuitionFee.equals(otherCommand.tuitionFee);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(index, paymentDate, tuitionFee);
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
