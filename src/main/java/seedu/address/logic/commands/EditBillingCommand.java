package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;

import java.time.LocalDate;
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
 * Edits billing fields of an existing person in the address book.
 */
public class EditBillingCommand extends EditCommand {

    public static final String SUB_COMMAND_WORD = "billing";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + SUB_COMMAND_WORD
            + ": Updates billing configuration for the student identified by the index number used "
            + "in the displayed student list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_AMOUNT + "AMOUNT] [" + PREFIX_DATE + "DATE]\n"
            + "Example: " + COMMAND_WORD + " " + SUB_COMMAND_WORD + " 1 "
            + PREFIX_AMOUNT + "50\n"
            + COMMAND_WORD + " " + SUB_COMMAND_WORD + " 1 "
            + PREFIX_DATE + "2026-03-28\n"
            + COMMAND_WORD + " " + SUB_COMMAND_WORD + " 1 "
            + PREFIX_AMOUNT + "70 " + PREFIX_DATE + "2026-03-01";

    public static final String MESSAGE_EDIT_TUITION_FEE_SUCCESS = "Updated tuition fee for %1$s: $%2$.2f";
    public static final String MESSAGE_EDIT_PAYMENT_DUE_SUCCESS = "Updated payment due date for %1$s: %2$s";
    public static final String MESSAGE_EDIT_BILLING_SUCCESS =
            "Updated billing for %1$s: tuition fee=$%2$.2f, payment due date=%3$s";

    private final Optional<Double> tuitionFee;
    private final Optional<LocalDate> paymentDue;

    /**
     * Creates an edit billing command that updates only tuition fee.
     */
    public EditBillingCommand(Index index, double tuitionFee) {
        this(index, Optional.of(tuitionFee), Optional.empty());
    }

    /**
     * Creates an edit billing command that updates only payment due date.
     */
    public EditBillingCommand(Index index, LocalDate paymentDue) {
        this(index, Optional.empty(), Optional.of(paymentDue));
    }

    /**
     * @param index of the person in the filtered person list to edit
     * @param tuitionFee optional tuition fee to set
     * @param paymentDue optional payment due date to set
     */
    public EditBillingCommand(Index index, Optional<Double> tuitionFee, Optional<LocalDate> paymentDue) {
        super(index);
        requireNonNull(tuitionFee);
        requireNonNull(paymentDue);
        if (tuitionFee.isEmpty() && paymentDue.isEmpty()) {
            throw new IllegalArgumentException(MESSAGE_NOT_EDITED);
        }
        this.tuitionFee = tuitionFee;
        this.paymentDue = paymentDue;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Person personToEdit = getTargetPerson(model);
        Billing updatedBilling = personToEdit.getBilling();
        if (tuitionFee.isPresent()) {
            updatedBilling = updatedBilling.updateRate(tuitionFee.get());
        }
        if (paymentDue.isPresent()) {
            updatedBilling = updatedBilling.updatePaymentDueDate(paymentDue.get());
        }

        Person editedPerson = new PersonBuilder(personToEdit)
                .withBilling(updatedBilling)
                .build();

        replacePerson(model, personToEdit, editedPerson);

        if (tuitionFee.isPresent() && paymentDue.isPresent()) {
            return new CommandResult(String.format(MESSAGE_EDIT_BILLING_SUCCESS,
                    Messages.format(editedPerson),
                    updatedBilling.getTuitionFee(),
                    updatedBilling.getCurrentDueDate()),
                    editedPerson);
        }
        if (tuitionFee.isPresent()) {
            return new CommandResult(String.format(MESSAGE_EDIT_TUITION_FEE_SUCCESS,
                    Messages.format(editedPerson), updatedBilling.getTuitionFee()), editedPerson);
        }

        return new CommandResult(String.format(MESSAGE_EDIT_PAYMENT_DUE_SUCCESS,
                Messages.format(editedPerson), updatedBilling.getCurrentDueDate()), editedPerson);
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
                && tuitionFee.equals(otherCommand.tuitionFee)
                && paymentDue.equals(otherCommand.paymentDue);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(index, tuitionFee, paymentDue);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("tuitionFee", tuitionFee)
                .add("paymentDue", paymentDue)
                .toString();
    }
}
