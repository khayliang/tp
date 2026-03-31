package seedu.address.logic.commands;

import seedu.address.commons.core.index.Index;

/**
 * Deletes fields of an entity identified using it's displayed index from the address book.
 */
public abstract class DeleteCommand extends IndexedPersonCommand {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the particulars of the person identified by the index "
            + "number used in the displayed person list.\n"
            + "Format: " + COMMAND_WORD + " SUBCOMMAND_PARAMETERS INDEX (must be a positive integer)\n"
            + "Examples: " + COMMAND_WORD + " person 1 n/John Doe";

    public static final String MESSAGE_NOT_DELETED = "At least one field to delete must be provided.";

    protected DeleteCommand(Index index) {
        super(index);
    }
}
