package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.DateTimeUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.attendance.AttendanceHistory;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;
import seedu.address.model.recurrence.Recurrence;
import seedu.address.model.session.Appointment;
import seedu.address.model.session.ScheduledSession;

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

    public static final String MESSAGE_ADD_APPT_SUCCESS = "Added appointment session for %1$s at %2$s.";
    public static final String MESSAGE_DUPLICATE_APPOINTMENT_DESCRIPTION =
            "Appointment description \"%1$s\" already exists for %2$s";

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
        Person personToEdit = IndexedPersonResolver.getTargetPerson(model, index);
        if (personToEdit.getAppointment().hasSessionWithDescription(description)) {
            throw new CommandException(String.format(MESSAGE_DUPLICATE_APPOINTMENT_DESCRIPTION,
                    description.trim(), Messages.format(personToEdit)));
        }
        ScheduledSession session = new ScheduledSession(recurrence, appointmentStart, appointmentStart,
            AttendanceHistory.EMPTY, description);
        Appointment updatedAppointment = personToEdit.getAppointment().addSession(session);
        Person editedPerson = new PersonBuilder(personToEdit)
            .withAppointment(updatedAppointment)
                .build();

        model.setPerson(personToEdit, editedPerson);
        String formattedStart = appointmentStart.format(DateTimeUtil.ISO_LOCAL_DATE_TIME_FORMATTER);
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
    public int hashCode() {
        return java.util.Objects.hash(index, appointmentStart, recurrence, description);
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
