package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.attendance.Attendance;
import seedu.address.model.attendance.AttendanceRecords;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;
import seedu.address.model.recurrence.Recurrence;
import seedu.address.model.session.Appointment;

/**
 * Adds an attendance record to a selected appointment of an existing person in the address book.
 */
public class AddAttdCommand extends AddCommand {

    public static final String SUB_COMMAND_WORD = "attd";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + SUB_COMMAND_WORD
            + ": Records attendance for the selected appointment of the student identified by the index number used "
            + "in the displayed student list.\n"
            + "Parameters: PERSON_INDEX (must be a positive integer) APPT_INDEX (must be a positive integer) "
            + "[y|n] [d/DATE_OR_DATE_TIME]\n"
            + "Example: " + COMMAND_WORD + " " + SUB_COMMAND_WORD + " 1 2 d/2026-01-29T08:00:00";

    public static final String MESSAGE_ADD_ATTD_SUCCESS = "Recorded attendance for %1$s: %2$s on %3$s";
    public static final String MESSAGE_INVALID_APPOINTMENT_INDEX =
            "The appointment index provided is invalid for the selected student.";
    public static final String MESSAGE_NON_RECURRING_ATTENDANCE_ALREADY_RECORDED =
            "Attendance has already been recorded for this non-recurring appointment.";
    public static final String MESSAGE_FUTURE_ATTENDANCE_NOT_ALLOWED =
            "Attendance cannot be recorded for a future date or time.";

    private static final DateTimeFormatter ATTENDANCE_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final Index personIndex;
    private final Index appointmentIndex;
    private final boolean hasAttended;
    private final Optional<LocalDateTime> recordedAt;

    /**
     * Creates an {@code AddAttdCommand}.
     */
    public AddAttdCommand(Index personIndex, Index appointmentIndex,
                          boolean hasAttended, Optional<LocalDateTime> recordedAt) {
        requireNonNull(personIndex);
        requireNonNull(appointmentIndex);
        requireNonNull(recordedAt);
        this.personIndex = personIndex;
        this.appointmentIndex = appointmentIndex;
        this.hasAttended = hasAttended;
        this.recordedAt = recordedAt;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Person personToEdit = getTargetPerson(model);
        Appointment appointment = getTargetAppointment(personToEdit);
        if (appointment.getRecurrence() == Recurrence.NONE && !appointment.getAttendance().isEmpty()) {
            throw new CommandException(MESSAGE_NON_RECURRING_ATTENDANCE_ALREADY_RECORDED);
        }

        LocalDateTime effectiveRecordedAt = recordedAt
                .orElseGet(() -> appointment.getNext().toLocalDate().atStartOfDay());
        if (effectiveRecordedAt.isAfter(LocalDateTime.now())) {
            throw new CommandException(MESSAGE_FUTURE_ATTENDANCE_NOT_ALLOWED);
        }
        Attendance attendanceRecord = new Attendance(hasAttended, effectiveRecordedAt);
        AttendanceRecords updatedAttendance = appointment.getAttendance().addAttendance(attendanceRecord);
        List<Appointment> updatedAppointments = new ArrayList<>(personToEdit.getAppointments());
        int currentAppointmentIndex = updatedAppointments.indexOf(appointment);
        updatedAppointments.set(currentAppointmentIndex, appointment.withAttendance(updatedAttendance));
        Person editedPerson = new PersonBuilder(personToEdit)
                .withAppointments(updatedAppointments)
                .build();

        model.setPerson(personToEdit, editedPerson);
        return new CommandResult(String.format(MESSAGE_ADD_ATTD_SUCCESS,
                Messages.format(editedPerson), hasAttended ? "present" : "absent",
                formatRecordedAt(effectiveRecordedAt)), editedPerson);
    }

    private String formatRecordedAt(LocalDateTime recordedAt) {
        if (recordedAt.toLocalTime().equals(LocalTime.MIDNIGHT)) {
            return recordedAt.toLocalDate().toString();
        }
        return recordedAt.format(ATTENDANCE_DATE_TIME_FORMATTER);
    }

    private Person getTargetPerson(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (personIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        return lastShownList.get(personIndex.getZeroBased());
    }

    private Appointment getTargetAppointment(Person person) throws CommandException {
        requireNonNull(person);
        List<Appointment> appointments = person.getAppointments();
        if (appointmentIndex.getZeroBased() >= appointments.size()) {
            throw new CommandException(MESSAGE_INVALID_APPOINTMENT_INDEX);
        }

        return appointments.get(appointmentIndex.getZeroBased());
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
                && appointmentIndex.equals(otherCommand.appointmentIndex)
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
                .add("appointmentIndex", appointmentIndex)
                .add("hasAttended", hasAttended)
                .add("recordedAt", formattedRecordedAt)
                .toString();
    }
}
