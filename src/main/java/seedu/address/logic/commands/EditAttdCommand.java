package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonBuilder;

/**
 * Edits the last attendance date-time of an existing person in the address book.
 */
public class EditAttdCommand extends EditCommand {

    public static final String SUB_COMMAND_WORD = "attd";

    public static final String MESSAGE_USAGE = COMMAND_WORD + " " + SUB_COMMAND_WORD
            + ": Records the last attendance date-time for the person identified "
            + "by the index number used in the displayed person list.\\n"
            + "Parameters: attd INDEX (must be a positive integer) [d/DATETIME]\\n"
            + "Example: " + COMMAND_WORD + " " + SUB_COMMAND_WORD + " 1 d/2026-01-29T08:00:00";

    public static final String MESSAGE_EDIT_ATTD_SUCCESS = "Recorded last attendance for %1$s: %2$s";

    private final Optional<LocalDateTime> attendanceToSet;

    /**
     * @param index of the person in the filtered person list to edit
     * @param attendanceToSet optional last attendance date-time to set
     */
    public EditAttdCommand(Index index, Optional<LocalDateTime> attendanceToSet) {
        super(index);
        requireNonNull(attendanceToSet);
        this.attendanceToSet = attendanceToSet;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        LocalDateTime lastAttendance = attendanceToSet.orElseGet(() -> LocalDateTime.now().withNano(0));
        Person personToEdit = getTargetPerson(model);
        Person editedPerson = new PersonBuilder(personToEdit)
                .withLastAttendance(Optional.of(lastAttendance))
                .build();

        replacePerson(model, personToEdit, editedPerson);
        String formattedAttendance = lastAttendance.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return new CommandResult(String.format(MESSAGE_EDIT_ATTD_SUCCESS,
                editedPerson.getName().fullName, formattedAttendance));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof EditAttdCommand)) {
            return false;
        }

        EditAttdCommand otherCommand = (EditAttdCommand) other;
        return index.equals(otherCommand.index)
            && attendanceToSet.equals(otherCommand.attendanceToSet);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("attendanceToSet", attendanceToSet)
                .toString();
    }
}
