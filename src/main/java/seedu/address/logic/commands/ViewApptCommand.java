package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.ListDisplayMode;
import seedu.address.model.Model;
import seedu.address.model.person.AppointmentInWeekPredicate;

/**
 * Shows all appointments that fall within a target week.
 */
public class ViewApptCommand extends Command {

    public static final String COMMAND_WORD = "viewAppt";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Shows appointments for the week containing the given date.\n"
            + "Parameters: [" + PREFIX_DATE + "DATE]\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_DATE + "2026-02-13";

    public static final String MESSAGE_SUCCESS = "%1$d appointments listed for week %2$s to %3$s";

    private final LocalDate targetDate;
    private final AppointmentInWeekPredicate predicate;

    /**
     * Creates a command that shows appointments for the week containing {@code targetDate}.
     */
    public ViewApptCommand(LocalDate targetDate) {
        requireNonNull(targetDate);
        this.targetDate = targetDate;
        this.predicate = new AppointmentInWeekPredicate(targetDate);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        model.setListDisplayMode(ListDisplayMode.APPOINTMENT);

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
        if (!(other instanceof ViewApptCommand)) {
            return false;
        }
        ViewApptCommand otherCommand = (ViewApptCommand) other;
        return targetDate.equals(otherCommand.targetDate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetDate", targetDate)
                .toString();
    }
}
