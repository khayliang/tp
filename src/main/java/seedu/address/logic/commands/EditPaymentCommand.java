package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPOINTMENT_START;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;

/**
 * Edits the date tuition fees were paid by an existing person in the address book.
 */
public class EditPaymentCommand extends EditCommand {

    public static final String SUB_COMMAND_WORD = "payment";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + SUB_COMMAND_WORD
        + ": Records the day tuition fees was paid by the person identified "
        + "by the index number used in the displayed person list.\n"
        + "Parameters: payment INDEX (must be a positive integer) "
        + PREFIX_APPOINTMENT_START + "DATE\n"
        + "Example: " + COMMAND_WORD + " " + SUB_COMMAND_WORD + " 1 "
        + PREFIX_APPOINTMENT_START + "2026-01-13";

    public static final String MESSAGE_EDIT_PAYMENT_SUCCESS = "Recorded date tuition fees paid by %1$s: %2$s";

    private final LocalDate paymentDate;

    /**
     * @param index of the person in the filtered person list to edit
     * @param appointmentStart appointment start date-time to set
     */
    public EditPaymentCommand(Index index, LocalDate paymentDate) {
        super(index);
        requireNonNull(paymentDate);
        this.paymentDate = paymentDate;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Person personToEdit = getTargetPerson(model);
        Person editedPerson = new PersonBuilder(personToEdit)
                .withPaymentDate(Optional.of(paymentDate))
                .build();

        replacePerson(model, personToEdit, editedPerson);
        String formattedDate = paymentDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        return new CommandResult(String.format(MESSAGE_EDIT_PAYMENT_SUCCESS,
                editedPerson.getName().fullName, formattedDate));
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
                && paymentDate.equals(otherCommand.paymentDate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("paymentDate", paymentDate)
                .toString();
    }
}
