package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.AppClock;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.attendance.Attendance;
import seedu.address.model.attendance.AttendanceHistory;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;
import seedu.address.model.recurrence.Recurrence;
import seedu.address.model.session.Appointment;
import seedu.address.model.session.ScheduledSession;

/**
 * Adds an attendance record to a selected appointment of an existing person in the address book.
 */
public class AddAttdCommand extends AddCommand {

    public static final String SUB_COMMAND_WORD = "attd";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + SUB_COMMAND_WORD
            + ": Records attendance for the selected appointment of the student identified by the index number used "
            + "in the displayed student list.\n"
            + "Parameters: INDEX (must be a positive integer) s/SESSION_INDEX [y|n] [d/DATE_OR_DATE_TIME]\n"
            + "Example: " + COMMAND_WORD + " " + SUB_COMMAND_WORD + " 1 s/2 y d/2026-01-29T08:00:00";

    public static final String MESSAGE_ADD_ATTD_SUCCESS = "Recorded %2$s attendance for %1$s on %3$s.";
    public static final String MESSAGE_RECURRING_NEXT_ADVANCED =
            "Recurring session next date advanced from %1$s to %2$s.";
    public static final String MESSAGE_RECURRING_NEXT_UNCHANGED =
            "Recurring session next date remains %1$s because the added attendance is not later than the latest "
                    + "record.";
    public static final String MESSAGE_NON_RECURRING_NEXT_UNCHANGED =
            "Session is non-recurring; no next date was advanced.";
    public static final String MESSAGE_INVALID_APPOINTMENT_INDEX =
            "The appointment index provided is invalid for the selected student.";
    public static final String MESSAGE_NON_RECURRING_ATTENDANCE_ALREADY_RECORDED =
            "Attendance has already been recorded for this non-recurring appointment.";
    public static final String MESSAGE_RECURRING_ATTENDANCE_ALREADY_RECORDED_ON_DATE =
            "Attendance has already been recorded for this recurring appointment on %1$s.";
    public static final String MESSAGE_FUTURE_ATTENDANCE_NOT_ALLOWED =
            "Attendance cannot be recorded for a future date or time.";

    private static final DateTimeFormatter ATTENDANCE_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final Index personIndex;
    private final Index sessionIndex;
    private final boolean hasAttended;
    private final Optional<LocalDateTime> recordedAt;

    /**
     * Creates an {@code AddAttdCommand}.
     */
    public AddAttdCommand(Index personIndex, Index sessionIndex,
                          boolean hasAttended, Optional<LocalDateTime> recordedAt) {
        requireNonNull(personIndex);
        requireNonNull(sessionIndex);
        requireNonNull(recordedAt);
        this.personIndex = personIndex;
        this.sessionIndex = sessionIndex;
        this.hasAttended = hasAttended;
        this.recordedAt = recordedAt;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Person personToEdit = IndexedPersonResolver.getTargetPerson(model, personIndex);
        ScheduledSession session = getTargetSession(personToEdit);
        LocalDateTime previousNext = session.getNext();
        if (session.getRecurrence() == Recurrence.NONE && !session.getAttendanceHistory().isEmpty()) {
            throw new CommandException(MESSAGE_NON_RECURRING_ATTENDANCE_ALREADY_RECORDED);
        }

        LocalDateTime effectiveRecordedAt = recordedAt
                .orElseGet(() -> session.getNext().toLocalDate().atStartOfDay());
        if (effectiveRecordedAt.isAfter(AppClock.now())) {
            throw new CommandException(MESSAGE_FUTURE_ATTENDANCE_NOT_ALLOWED);
        }
        if (session.getRecurrence() != Recurrence.NONE
            && session.getAttendanceHistory().hasRecordOn(effectiveRecordedAt.toLocalDate())) {
            throw new CommandException(String.format(MESSAGE_RECURRING_ATTENDANCE_ALREADY_RECORDED_ON_DATE,
                effectiveRecordedAt.toLocalDate()));
        }
        boolean shouldAdvanceRecurringNext = session.getRecurrence() != Recurrence.NONE
            && session.getAttendanceHistory().getLatestRecordByRecordedAt()
            .map(latestRecord -> effectiveRecordedAt.isAfter(latestRecord.getRecordedAt()))
            .orElse(true);
        Attendance attendanceRecord = new Attendance(hasAttended, effectiveRecordedAt);
        AttendanceHistory updatedAttendance = session.getAttendanceHistory().addAttendance(attendanceRecord);
        ScheduledSession updatedSession = session.withAttendance(updatedAttendance);
        if (shouldAdvanceRecurringNext) {
            updatedSession = updatedSession.withAdvancedNext();
        }
        int currentSessionIndex = sessionIndex.getZeroBased();
        java.util.List<ScheduledSession> updatedSessions =
            new java.util.ArrayList<>(personToEdit.getAppointment().getSessions());
        updatedSessions.set(currentSessionIndex, updatedSession);
        Person editedPerson = new PersonBuilder(personToEdit)
                .withAppointment(new Appointment(updatedSessions))
                .build();

        model.setPerson(personToEdit, editedPerson);
        String attendanceFeedback = String.format(MESSAGE_ADD_ATTD_SUCCESS,
                Messages.format(editedPerson), hasAttended ? "present" : "absent",
                formatRecordedAt(effectiveRecordedAt));
        String nextSessionFeedback = buildNextSessionFeedback(
                session.getRecurrence(), previousNext, updatedSession.getNext());
        return new CommandResult(attendanceFeedback + " " + nextSessionFeedback, editedPerson);
    }

    private String formatRecordedAt(LocalDateTime recordedAt) {
        if (recordedAt.toLocalTime().equals(LocalTime.MIDNIGHT)) {
            return recordedAt.toLocalDate().toString();
        }
        return recordedAt.format(ATTENDANCE_DATE_TIME_FORMATTER);
    }

    private String buildNextSessionFeedback(Recurrence recurrence,
                                            LocalDateTime previousNext,
                                            LocalDateTime updatedNext) {
        if (recurrence == Recurrence.NONE) {
            return MESSAGE_NON_RECURRING_NEXT_UNCHANGED;
        }

        if (updatedNext.isAfter(previousNext)) {
            return String.format(MESSAGE_RECURRING_NEXT_ADVANCED,
                    formatSessionNext(previousNext),
                    formatSessionNext(updatedNext));
        }

        return String.format(MESSAGE_RECURRING_NEXT_UNCHANGED, formatSessionNext(updatedNext));
    }

    private String formatSessionNext(LocalDateTime nextDateTime) {
        return nextDateTime.format(ATTENDANCE_DATE_TIME_FORMATTER);
    }

    private ScheduledSession getTargetSession(Person person) throws CommandException {
        requireNonNull(person);
        java.util.List<ScheduledSession> sessions = person.getAppointment().getSessions();
        if (sessionIndex.getZeroBased() >= sessions.size()) {
            throw new CommandException(MESSAGE_INVALID_APPOINTMENT_INDEX);
        }

        return sessions.get(sessionIndex.getZeroBased());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AddAttdCommand)) {
            return false;
        }

        AddAttdCommand otherCommand = (AddAttdCommand) other;
        return personIndex.equals(otherCommand.personIndex)
            && sessionIndex.equals(otherCommand.sessionIndex)
                && hasAttended == otherCommand.hasAttended
                && recordedAt.equals(otherCommand.recordedAt);
    }

    @Override
    public String toString() {
        String formattedRecordedAt = recordedAt
                .map(this::formatRecordedAt)
                .orElse(null);
        return new ToStringBuilder(this)
                .add("personIndex", personIndex)
                .add("sessionIndex", sessionIndex)
                .add("hasAttended", hasAttended)
                .add("recordedAt", formattedRecordedAt)
                .toString();
    }
}
