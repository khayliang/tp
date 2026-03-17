package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPOINTMENT_START;

import java.time.LocalDateTime;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.DateTimeUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;

/**
 * Edits the appointment start date-time of an existing person in the address book.
 */
public class EditApptCommand extends EditCommand {

    public static final String SUB_COMMAND_WORD = "appt";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + SUB_COMMAND_WORD
            + ": Records the lesson start date-time for the person identified "
            + "by the index number used in the displayed person list.\n"
            + "Parameters: appt INDEX (must be a positive integer) "
            + PREFIX_APPOINTMENT_START + "DATETIME\n"
            + "Example: " + COMMAND_WORD + " " + SUB_COMMAND_WORD + " 1 "
            + PREFIX_APPOINTMENT_START + "2026-01-13T08:00:00";

    public static final String MESSAGE_EDIT_APPT_SUCCESS = "Recorded lesson start date for %1$s: %2$s";

    private final LocalDateTime appointmentStart;

    /**
     * @param index of the person in the filtered person list to edit
     * @param appointmentStart appointment start date-time to set
     */
    public EditApptCommand(Index index, LocalDateTime appointmentStart) {
        super(index);
        requireNonNull(appointmentStart);
        this.appointmentStart = appointmentStart;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Person personToEdit = getTargetPerson(model);
        Person editedPerson = new PersonBuilder(personToEdit)
                .withAppointmentStart(Optional.of(appointmentStart))
                .build();

        replacePerson(model, personToEdit, editedPerson);
        String formattedStart = DateTimeUtil.formatIsoLocalDateTime(appointmentStart);
        return new CommandResult(String.format(MESSAGE_EDIT_APPT_SUCCESS,
                editedPerson.getName().fullName, formattedStart));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof EditApptCommand)) {
            return false;
        }

        EditApptCommand otherCommand = (EditApptCommand) other;
        return index.equals(otherCommand.index)
                && appointmentStart.equals(otherCommand.appointmentStart);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("appointmentStart", appointmentStart)
                .toString();
    }
}
