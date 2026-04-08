package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.DateTimeUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;
import seedu.address.model.session.ScheduledSession;

/**
 * Deletes an appointment from an existing person in the address book.
 */
public class DeleteApptCommand extends DeleteCommand {

    public static final String SUB_COMMAND_WORD = "appt";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + SUB_COMMAND_WORD
            + ": Deletes one or more sessions from the student identified by the index used in the displayed list.\n"
            + "Parameters: INDEX s/SESSION_INDEX [s/SESSION_INDEX]...\n"
            + "Example: " + COMMAND_WORD + " " + SUB_COMMAND_WORD + " 1 s/2 s/3";

    public static final String MESSAGE_DELETE_APPT_SUCCESS = "Removed appointment session(s) for %1$s: %2$s.";
    public static final String MESSAGE_INVALID_APPOINTMENT_INDEX =
            "One or more session indices provided are invalid for the selected student.";

    private final List<Index> sessionIndices;

    /**
     * Creates a {@code DeleteApptCommand}.
     */
    public DeleteApptCommand(Index personIndex, List<Index> sessionIndices) {
        super(personIndex);
        requireNonNull(sessionIndices);
        this.sessionIndices = List.copyOf(sessionIndices);
    }

    /**
     * Creates a {@code DeleteApptCommand} for a single session index.
     */
    public DeleteApptCommand(Index personIndex, Index sessionIndex) {
        this(personIndex, List.of(sessionIndex));
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Person personToEdit = IndexedPersonResolver.getTargetPerson(model, index);
        List<ScheduledSession> sessions = personToEdit.getAppointment().getSessions();
        validateSessionIndices(sessions);
        String deletedSessionsText = formatSessions(sessions);

        Person editedPerson = new PersonBuilder(personToEdit)
                .withAppointment(personToEdit.getAppointment().removeSessions(sessionIndices))
                .build();

        replacePerson(model, personToEdit, editedPerson);
        return new CommandResult(String.format(MESSAGE_DELETE_APPT_SUCCESS,
                Messages.format(editedPerson), deletedSessionsText), editedPerson);
    }

    private void validateSessionIndices(List<ScheduledSession> sessions) throws CommandException {
        for (Index sessionIndex : sessionIndices) {
            if (sessionIndex.getZeroBased() >= sessions.size()) {
                throw new CommandException(MESSAGE_INVALID_APPOINTMENT_INDEX);
            }
        }
    }

    private String formatSessions(List<ScheduledSession> sessions) {
        List<String> formattedSessions = sessionIndices.stream()
            .map(index -> sessions.get(index.getZeroBased()).getStart()
                .format(DateTimeUtil.ISO_LOCAL_DATE_TIME_FORMATTER))
                .toList();
        if (formattedSessions.size() == 1) {
            return formattedSessions.get(0);
        }
        return formattedSessions.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DeleteApptCommand)) {
            return false;
        }

        DeleteApptCommand otherCommand = (DeleteApptCommand) other;
        return index.equals(otherCommand.index)
            && sessionIndices.equals(otherCommand.sessionIndices);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(index, sessionIndices);
    }

    @Override
    public String toString() {
        if (sessionIndices.size() == 1) {
            return new ToStringBuilder(this)
                    .add("personIndex", index)
                    .add("appointmentIndex", sessionIndices.get(0))
                    .toString();
        }
        return new ToStringBuilder(this)
                .add("personIndex", index)
            .add("sessionIndices", sessionIndices)
                .toString();
    }
}
