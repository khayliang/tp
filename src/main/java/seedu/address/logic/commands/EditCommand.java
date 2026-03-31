package seedu.address.logic.commands;

import seedu.address.commons.core.index.Index;

/**
 * Edits an entity in the address book via a subcommand.
 */
public abstract class EditCommand extends IndexedPersonCommand {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits an entity via a subcommand.\n"
            + "Format: " + COMMAND_WORD + " SUBCOMMAND PARAMETERS\n"
            + "Examples: " + COMMAND_WORD + " student 1 n/John Doe";

    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";

    protected EditCommand(Index index) {
        super(index);
    }
}
