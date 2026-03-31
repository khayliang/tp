package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ListDisplayMode;
import seedu.address.model.Model;
import seedu.address.model.attendance.Attendance;
import seedu.address.model.attendance.AttendanceRecords;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;
import seedu.address.model.person.PersonComparators;
import seedu.address.model.recurrence.Recurrence;
import seedu.address.model.session.Appointment;

/**
 * Adds an attendance record to the current appointment of an existing person in the address book.
 */
public class AddAttdCommand extends AddCommand {

    public static final String SUB_COMMAND_WORD = "attd";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + SUB_COMMAND_WORD
            + ": Records attendance for the current appointment of the student identified by the index number used "
            + "in the displayed student list.\n"
            + "Parameters: INDEX (must be a positive integer) [y|n] [d/DATE]\n"
            + "Example: " + COMMAND_WORD + " " + SUB_COMMAND_WORD + " 1 d/2026-01-29";

    public static final String MESSAGE_ADD_ATTD_SUCCESS = "Recorded attendance for %1$s: %2$s on %3$s";
    public static final String MESSAGE_NO_CURRENT_APPOINTMENT =
            "The selected student does not have a current appointment.";
    public static final String MESSAGE_NON_RECURRING_ATTENDANCE_ALREADY_RECORDED =
            "Attendance has already been recorded for this non-recurring appointment.";
    public static final String MESSAGE_FUTURE_ATTENDANCE_NOT_ALLOWED =
            "Attendance cannot be recorded for a future date.";

    private final Index index;
    private final boolean hasAttended;
    private final Optional<LocalDate> recordedDateOverride;

    /**
     * Creates an {@code AddAttdCommand}.
     */
    public AddAttdCommand(Index index, boolean hasAttended, Optional<LocalDate> recordedDateOverride) {
        requireNonNull(index);
        requireNonNull(recordedDateOverride);
        this.index = index;
        this.hasAttended = hasAttended;
        this.recordedDateOverride = recordedDateOverride;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        Person personToEdit = getTargetPerson(model);
        Appointment appointment = personToEdit.getAppointment()
                .orElseThrow(() -> new CommandException(MESSAGE_NO_CURRENT_APPOINTMENT));
        if (appointment.getRecurrence() == Recurrence.NONE && !appointment.getAttendance().isEmpty()) {
            throw new CommandException(MESSAGE_NON_RECURRING_ATTENDANCE_ALREADY_RECORDED);
        }

        LocalDate defaultRecordedDate = appointment.getNext().toLocalDate();
        LocalDate recordedDate = recordedDateOverride.orElse(defaultRecordedDate);
        if (recordedDate.isAfter(LocalDate.now())) {
            throw new CommandException(MESSAGE_FUTURE_ATTENDANCE_NOT_ALLOWED);
        }
        Attendance attendanceRecord = new Attendance(hasAttended, recordedDate);
        AttendanceRecords updatedAttendance = appointment.getAttendance().addAttendance(attendanceRecord);
        Person editedPerson = new PersonBuilder(personToEdit)
                .withAppointment(appointment.withAttendance(updatedAttendance))
                .build();

        model.setPerson(personToEdit, editedPerson);
        return new CommandResult(String.format(MESSAGE_ADD_ATTD_SUCCESS,
                Messages.format(editedPerson), hasAttended ? "present" : "absent", recordedDate), editedPerson);
    }

    private Person getTargetPerson(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = getDisplayedPersonList(model);

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        return lastShownList.get(index.getZeroBased());
    }

    private List<Person> getDisplayedPersonList(Model model) {
        if (model.getListDisplayMode() == ListDisplayMode.APPOINTMENT) {
            return model.getFilteredPersonList().stream()
                    .sorted(PersonComparators.APPOINTMENT_ORDER)
                    .toList();
        }
        return model.getFilteredPersonList();
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
        return index.equals(otherCommand.index)
                && hasAttended == otherCommand.hasAttended
                && recordedDateOverride.equals(otherCommand.recordedDateOverride);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("hasAttended", hasAttended)
                .add("recordedDateOverride", recordedDateOverride.orElse(null))
                .toString();
    }
}
