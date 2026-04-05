package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.attendance.AttendanceRecords;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;
import seedu.address.model.recurrence.Recurrence;
import seedu.address.model.session.Appointment;

/**
 * Adds an appointment to an existing person in the address book.
 */
public class AddApptCommand extends AddCommand {

    public static final String SUB_COMMAND_WORD = "appt";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + SUB_COMMAND_WORD
            + ": Adds an appointment for the student identified by the index number used "
            + "in the displayed student list.\n"
            + "Parameters: INDEX (must be a positive integer) d/DATETIME [r/RECURRENCE] dsc/DESCRIPTION\n"
            + "Example: " + COMMAND_WORD + " " + SUB_COMMAND_WORD + " 1 "
            + "d/2026-01-13T08:00:00 r/NONE dsc/Weekly algebra practice";

    public static final String MESSAGE_ADD_APPT_SUCCESS = "Added appointment for %1$s: %2$s";

    private final Index index;
    private final LocalDateTime appointmentStart;
    private final Recurrence recurrence;
    private final String description;

    /**
     * Creates an {@code AddApptCommand}.
     */
    public AddApptCommand(Index index, LocalDateTime appointmentStart,
                          Recurrence recurrence, String description) {
        requireNonNull(index);
        requireNonNull(appointmentStart);
        requireNonNull(recurrence);
        requireNonNull(description);
        this.index = index;
        this.appointmentStart = appointmentStart;
        this.recurrence = recurrence;
        this.description = description;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Person personToEdit = getTargetPerson(model, index);
        Appointment appointment = new Appointment(recurrence, appointmentStart, appointmentStart,
                AttendanceRecords.EMPTY, description);
        Person editedPerson = new PersonBuilder(personToEdit)
                .addAppointment(appointment)
                .build();

        model.setPerson(personToEdit, editedPerson);
        String formattedStart = appointmentStart.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return new CommandResult(String.format(MESSAGE_ADD_APPT_SUCCESS,
                Messages.format(editedPerson), formattedStart), editedPerson);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AddApptCommand)) {
            return false;
        }

        AddApptCommand otherCommand = (AddApptCommand) other;
        return index.equals(otherCommand.index)
                && appointmentStart.equals(otherCommand.appointmentStart)
                && recurrence.equals(otherCommand.recurrence)
                && description.equals(otherCommand.description);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("appointmentStart", appointmentStart)
                .add("recurrence", recurrence)
                .add("description", description)
                .toString();
    }
}
