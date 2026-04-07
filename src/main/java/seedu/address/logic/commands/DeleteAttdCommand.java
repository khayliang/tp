package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SESSION;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
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
 * Deletes attendance record(s) for a selected session of an existing person in the address book.
 */
public class DeleteAttdCommand extends DeleteCommand {

    public static final String SUB_COMMAND_WORD = "attd";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + SUB_COMMAND_WORD
            + ": Deletes attendance records for the selected appointment of the student identified by the index "
            + "number used in the displayed student list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_SESSION + "SESSION_INDEX "
            + PREFIX_DATE + "DATE_OR_DATE_TIME\n"
            + "Example: " + COMMAND_WORD + " " + SUB_COMMAND_WORD + " 1 "
            + PREFIX_SESSION + "2 " + PREFIX_DATE + "2026-01-29T08:00:00";

    public static final String MESSAGE_DELETE_ATTD_SUCCESS =
            "Deleted attendance for %1$s from session %2$s on %3$s.";
    public static final String MESSAGE_RECURRING_NEXT_ROLLED_BACK =
            "Recurring session next date rolled back from %1$s to %2$s.";
    public static final String MESSAGE_RECURRING_NEXT_UNCHANGED =
            "Recurring session next date remains %1$s.";
    public static final String MESSAGE_NON_RECURRING_NEXT_UNCHANGED =
            "Session is non-recurring; no next date was rolled back.";
    public static final String MESSAGE_INVALID_APPOINTMENT_INDEX =
            "The appointment index provided is invalid for the selected student.";
    public static final String MESSAGE_ATTENDANCE_NOT_FOUND =
            "No attendance record found for the selected session on %1$s.";

    private static final DateTimeFormatter ATTENDANCE_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final Index sessionIndex;
    private final Optional<LocalDateTime> recordedAt;
    private final Optional<LocalDate> recordedDate;

    /**
     * Creates a {@code DeleteAttdCommand} that deletes attendance by exact date-time.
     */
    public DeleteAttdCommand(Index personIndex, Index sessionIndex, LocalDateTime recordedAt) {
        this(personIndex, sessionIndex, Optional.of(recordedAt), Optional.empty());
    }

    /**
     * Creates a {@code DeleteAttdCommand} that deletes attendance by date.
     */
    public DeleteAttdCommand(Index personIndex, Index sessionIndex, LocalDate recordedDate) {
        this(personIndex, sessionIndex, Optional.empty(), Optional.of(recordedDate));
    }

    private DeleteAttdCommand(Index personIndex, Index sessionIndex,
                              Optional<LocalDateTime> recordedAt, Optional<LocalDate> recordedDate) {
        super(personIndex);
        requireNonNull(sessionIndex);
        requireNonNull(recordedAt);
        requireNonNull(recordedDate);
        if (recordedAt.isPresent() == recordedDate.isPresent()) {
            throw new IllegalArgumentException("Exactly one attendance target must be provided.");
        }
        this.sessionIndex = sessionIndex;
        this.recordedAt = recordedAt;
        this.recordedDate = recordedDate;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Person personToEdit = getTargetPerson(model);
        List<ScheduledSession> sessions = personToEdit.getAppointment().getSessions();
        validateSessionIndex(sessions);

        int targetSessionZeroBased = sessionIndex.getZeroBased();
        ScheduledSession session = sessions.get(targetSessionZeroBased);
        LocalDateTime previousNext = session.getNext();
        AttendanceHistory attendanceHistory = session.getAttendanceHistory();

        boolean shouldRollback = isLatestRecordBeingDeleted(attendanceHistory);
        AttendanceHistory updatedAttendanceHistory = deleteAttendance(attendanceHistory);

        ScheduledSession updatedSession = session.withAttendance(updatedAttendanceHistory);
        if (shouldRollback && session.getRecurrence() != Recurrence.NONE) {
            updatedSession = updatedSession.withRolledBackNext();
        }

        Appointment updatedAppointment = personToEdit.getAppointment()
            .withSessionAt(targetSessionZeroBased, updatedSession);
        Person editedPerson = new PersonBuilder(personToEdit)
                .withAppointment(updatedAppointment)
                .build();

        replacePerson(model, personToEdit, editedPerson);
        String deletionFeedback = String.format(MESSAGE_DELETE_ATTD_SUCCESS,
                Messages.format(editedPerson),
                sessionIndex.getOneBased(),
            formatTarget());
        String scheduleFeedback = buildScheduleFeedback(
            session.getRecurrence(), previousNext, updatedSession.getNext());

        return new CommandResult(deletionFeedback + " " + scheduleFeedback, editedPerson);
    }

    private void validateSessionIndex(List<ScheduledSession> sessions) throws CommandException {
        if (sessionIndex.getZeroBased() >= sessions.size()) {
            throw new CommandException(MESSAGE_INVALID_APPOINTMENT_INDEX);
        }
    }

    private boolean isLatestRecordBeingDeleted(AttendanceHistory history) {
        return history.getLatestRecordByRecordedAt()
                .map(latestRecord -> {
                    if (recordedAt.isPresent()) {
                        return latestRecord.getRecordedAt().equals(recordedAt.get());
                    }
                    return latestRecord.getRecordedAt().toLocalDate().equals(recordedDate.get());
                })
                .orElse(false);
    }

    private AttendanceHistory deleteAttendance(AttendanceHistory history) throws CommandException {
        if (recordedAt.isPresent()) {
            LocalDateTime targetDateTime = recordedAt.get();
            if (!history.hasRecordAt(targetDateTime)) {
                throw new CommandException(String.format(MESSAGE_ATTENDANCE_NOT_FOUND, formatTarget()));
            }
            return history.removeAttendance(targetDateTime);
        }

        LocalDate targetDate = recordedDate.get();
        if (!history.hasRecordOn(targetDate)) {
            throw new CommandException(String.format(MESSAGE_ATTENDANCE_NOT_FOUND, formatTarget()));
        }
        return history.removeAttendance(targetDate);
    }

    private String formatTarget() {
        if (recordedAt.isPresent()) {
            LocalDateTime targetDateTime = recordedAt.get();
            if (targetDateTime.toLocalTime().equals(LocalTime.MIDNIGHT)) {
                return targetDateTime.toLocalDate().toString();
            }
            return targetDateTime.format(ATTENDANCE_DATE_TIME_FORMATTER);
        }
        return recordedDate.get().toString();
    }

    private String buildScheduleFeedback(Recurrence recurrence,
                                         LocalDateTime previousNext,
                                         LocalDateTime updatedNext) {
        if (recurrence == Recurrence.NONE) {
            return MESSAGE_NON_RECURRING_NEXT_UNCHANGED;
        }

        if (updatedNext.isBefore(previousNext)) {
            return String.format(MESSAGE_RECURRING_NEXT_ROLLED_BACK,
                    formatSessionNext(previousNext),
                    formatSessionNext(updatedNext));
        }

        return String.format(MESSAGE_RECURRING_NEXT_UNCHANGED, formatSessionNext(updatedNext));
    }

    private String formatSessionNext(LocalDateTime nextDateTime) {
        return nextDateTime.format(ATTENDANCE_DATE_TIME_FORMATTER);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof DeleteAttdCommand)) {
            return false;
        }

        DeleteAttdCommand otherCommand = (DeleteAttdCommand) other;
        return index.equals(otherCommand.index)
                && sessionIndex.equals(otherCommand.sessionIndex)
                && recordedAt.equals(otherCommand.recordedAt)
                && recordedDate.equals(otherCommand.recordedDate);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(index, sessionIndex, recordedAt, recordedDate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("personIndex", index)
                .add("sessionIndex", sessionIndex)
                .add("recordedAt", recordedAt.orElse(null))
                .add("recordedDate", recordedDate.orElse(null))
                .toString();
    }
}
