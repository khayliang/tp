package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.Model;
import seedu.address.model.person.AppointmentInWeekPredicate;

/**
 * Finds and lists all students whose appointments fall within the week containing the given date.
 */
public class FindApptCommand extends FindCommand {

    public static final String SUB_COMMAND_WORD = "appt";

    public static final String MESSAGE_USAGE = FindCommand.COMMAND_WORD + " " + SUB_COMMAND_WORD
            + ": Finds all students whose appointments fall within the Monday-Sunday week containing the given "
            + "date.\n"
            + "Parameters: [" + PREFIX_DATE + "DATE]\n"
            + "Example: " + FindCommand.COMMAND_WORD + " " + SUB_COMMAND_WORD + " " + PREFIX_DATE + "2026-02-13";

    public static final String MESSAGE_SUCCESS = "%1$d students listed for week %2$s to %3$s";

    private final LocalDate targetDate;
    private final AppointmentInWeekPredicate predicate;

    /**
     * Creates a command that filters students with appointments in the week containing {@code targetDate}.
     */
    public FindApptCommand(LocalDate targetDate) {
        requireNonNull(targetDate);
        this.targetDate = targetDate;
        this.predicate = new AppointmentInWeekPredicate(targetDate);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonListWithAnd(predicate);

        String weekStart = predicate.getWeekStart().format(DateTimeFormatter.ISO_LOCAL_DATE);
        String weekEnd = predicate.getWeekEnd().format(DateTimeFormatter.ISO_LOCAL_DATE);
        String feedback = String.format(MESSAGE_SUCCESS, model.getFilteredPersonList().size(), weekStart, weekEnd);
        return new CommandResult(feedback);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof FindApptCommand)) {
            return false;
        }
        FindApptCommand otherCommand = (FindApptCommand) other;
        return targetDate.equals(otherCommand.targetDate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetDate", targetDate)
                .toString();
    }
}
