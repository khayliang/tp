package seedu.address.logic.commands;

/**
 * Represents the base command word for all find-related subcommands.
 */
public abstract class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds entries by subcommand.\n"
            + "Parameters: SUBCOMMAND [ARGS]\n"
            + "Example: " + COMMAND_WORD + " student alice";
}
