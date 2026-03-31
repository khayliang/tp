package seedu.address.logic.parser;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeletePersonCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code DeletePersonCommand} object
 */
public class DeletePersonCommandParser implements Parser<DeletePersonCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeletePersonCommand parse(String args) throws ParseException {
        Index index = ParserUtil.parseIndex(args, DeletePersonCommand.MESSAGE_USAGE);
        return new DeletePersonCommand(index);
    }
}
