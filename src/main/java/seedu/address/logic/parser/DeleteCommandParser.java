package seedu.address.logic.parser;

import java.util.HashMap;
import java.util.Map;

import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeletePaymentCommand;
import seedu.address.logic.commands.DeletePersonCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteCommand object based on subcommand.
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    private final SubcommandDispatcherParser<DeleteCommand> dispatcher;

    /**
     * Creates a DeleteCommandParser and registers available sub-command parsers.
     */
    public DeleteCommandParser() {
        Map<String, Parser<? extends DeleteCommand>> parsers = new HashMap<>();
        parsers.put(DeletePersonCommand.SUB_COMMAND_WORD, new DeletePersonCommandParser());
        parsers.put(DeletePaymentCommand.SUB_COMMAND_WORD, new DeletePaymentCommandParser());
        this.dispatcher = new SubcommandDispatcherParser<>(parsers, DeleteCommand.MESSAGE_USAGE);
    }

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public DeleteCommand parse(String args) throws ParseException {
        return dispatcher.parse(args);
    }
}
