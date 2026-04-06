package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RECURRENCE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SESSION;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;
import seedu.address.model.recurrence.Recurrence;
import seedu.address.model.session.Appointment;
import seedu.address.model.session.ScheduledSession;

/**
 * Edits one scheduled appointment session for an existing person in the address book.
 */
public class EditApptCommand extends EditCommand {

    public static final String SUB_COMMAND_WORD = "appt";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + SUB_COMMAND_WORD
            + ": Edits a selected appointment session for the student identified by the index number used "
            + "in the displayed student list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_SESSION + "SESSION_INDEX "
            + "[" + PREFIX_DATE + "DATETIME] "
            + "[" + PREFIX_RECURRENCE + "RECURRENCE] "
            + "[" + PREFIX_DESCRIPTION + "DESCRIPTION]\n"
            + "Example: " + COMMAND_WORD + " " + SUB_COMMAND_WORD + " 1 "
            + PREFIX_SESSION + "2 " + PREFIX_DATE + "2026-02-12T09:00:00 "
            + PREFIX_RECURRENCE + "WEEKLY " + PREFIX_DESCRIPTION + "Physics consultation";

    public static final String MESSAGE_EDIT_APPT_SUCCESS = "Edited appointment for %1$s: %2$s";
    public static final String MESSAGE_INVALID_APPOINTMENT_INDEX =
            "The appointment index provided is invalid for the selected student.";
    public static final String MESSAGE_DUPLICATE_APPOINTMENT_DESCRIPTION =
            "Appointment description \"%1$s\" already exists for %2$s";

    private static final DateTimeFormatter APPOINTMENT_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final Index sessionIndex;
    private final Optional<LocalDateTime> appointmentStart;
    private final Optional<Recurrence> recurrence;
    private final Optional<String> description;

    /**
     * Creates an {@code EditApptCommand}.
     */
    public EditApptCommand(Index personIndex, Index sessionIndex,
                           Optional<LocalDateTime> appointmentStart,
                           Optional<Recurrence> recurrence,
                           Optional<String> description) {
        super(personIndex);
        requireNonNull(sessionIndex);
        requireNonNull(appointmentStart);
        requireNonNull(recurrence);
        requireNonNull(description);
        if (appointmentStart.isEmpty() && recurrence.isEmpty() && description.isEmpty()) {
            throw new IllegalArgumentException(MESSAGE_NOT_EDITED);
        }
        this.sessionIndex = sessionIndex;
        this.appointmentStart = appointmentStart;
        this.recurrence = recurrence;
        this.description = description;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Person personToEdit = getTargetPerson(model);
        List<ScheduledSession> sessions = personToEdit.getAppointment().getSessions();
        validateSessionIndex(sessions);

        ScheduledSession targetSession = sessions.get(sessionIndex.getZeroBased());
        validateDescriptionUniqueness(personToEdit, targetSession);
        ScheduledSession editedSession = createEditedSession(targetSession);

        Appointment updatedAppointment = personToEdit.getAppointment()
                .withSessionAt(sessionIndex.getZeroBased(), editedSession);
        Person editedPerson = new PersonBuilder(personToEdit)
                .withAppointment(updatedAppointment)
                .build();

        replacePerson(model, personToEdit, editedPerson);
        return new CommandResult(String.format(MESSAGE_EDIT_APPT_SUCCESS,
                Messages.format(editedPerson), formatSession(editedSession)), editedPerson);
    }

    private void validateSessionIndex(List<ScheduledSession> sessions) throws CommandException {
        if (sessionIndex.getZeroBased() >= sessions.size()) {
            throw new CommandException(MESSAGE_INVALID_APPOINTMENT_INDEX);
        }
    }

    private void validateDescriptionUniqueness(Person personToEdit,
                                               ScheduledSession targetSession) throws CommandException {
        if (description.isEmpty()) {
            return;
        }

        String updatedDescription = description.orElseThrow().trim();
        if (updatedDescription.equals(targetSession.getDescription())) {
            return;
        }

        if (personToEdit.getAppointment()
                .hasSessionWithDescriptionExcludingIndex(updatedDescription, sessionIndex.getZeroBased())) {
            throw new CommandException(String.format(MESSAGE_DUPLICATE_APPOINTMENT_DESCRIPTION,
                    updatedDescription, Messages.format(personToEdit)));
        }
    }

    private ScheduledSession createEditedSession(ScheduledSession session) {
        LocalDateTime updatedStart = appointmentStart.orElse(session.getStart());
        LocalDateTime updatedNext = appointmentStart.orElse(session.getNext());
        Recurrence updatedRecurrence = recurrence.orElse(session.getRecurrence());
        String updatedDescription = description.orElse(session.getDescription());

        return new ScheduledSession(updatedRecurrence,
                updatedStart,
                updatedNext,
                session.getAttendanceHistory(),
                updatedDescription);
    }

    private String formatSession(ScheduledSession session) {
        return session.getNext().format(APPOINTMENT_DATE_TIME_FORMATTER)
                + " (" + session.getRecurrence() + ", " + session.getDescription() + ")";
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
                && sessionIndex.equals(otherCommand.sessionIndex)
                && appointmentStart.equals(otherCommand.appointmentStart)
                && recurrence.equals(otherCommand.recurrence)
                && description.equals(otherCommand.description);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(index, sessionIndex, appointmentStart, recurrence, description);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("personIndex", index)
                .add("sessionIndex", sessionIndex)
                .add("appointmentStart", appointmentStart.orElse(null))
                .add("recurrence", recurrence.orElse(null))
                .add("description", description.orElse(null))
                .toString();
    }
}
