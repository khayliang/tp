package seedu.address.logic.commands;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

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

    /**
     * Returns the target person from the currently displayed person list.
     *
     * @throws CommandException if the index is out of bounds
     */
    protected Person getTargetPerson(Model model, Index index) throws CommandException {
        return IndexedPersonResolver.getTargetPerson(model, index);
    }
}
