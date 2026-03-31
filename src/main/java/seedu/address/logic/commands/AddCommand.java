package seedu.address.logic.commands;

/**
 * Adds an entity to the address book via a subcommand.
 */
public abstract class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an entity via a subcommand.\n"
            + "Format: " + COMMAND_WORD + " SUBCOMMAND PARAMETERS\n"
            + "Examples: " + COMMAND_WORD + " student n/John Doe p/98765432 "
            + "e/johnd@example.com a/311, Clementi Ave 2, #02-25, "
            + COMMAND_WORD + " appt 1 d/2026-01-13T08:00:00 dsc/Weekly algebra practice, "
            + COMMAND_WORD + " attd 1 y d/2026-01-29";
}
