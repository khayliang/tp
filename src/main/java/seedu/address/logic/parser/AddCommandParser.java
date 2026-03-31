package seedu.address.logic.parser;

import java.util.HashMap;
import java.util.Map;

import seedu.address.logic.commands.AddApptCommand;
import seedu.address.logic.commands.AddAttdCommand;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AddPersonCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new AddCommand object.
 */
public class AddCommandParser implements Parser<AddCommand> {

    private final SubcommandDispatcherParser<AddCommand> dispatcher;

    /**
     * Creates an AddCommandParser and registers available sub-command parsers.
     */
    public AddCommandParser() {
        Map<String, Parser<? extends AddCommand>> parsers = new HashMap<>();
        parsers.put(AddPersonCommand.SUB_COMMAND_WORD, new AddPersonCommandParser());
        parsers.put(AddApptCommand.SUB_COMMAND_WORD, new AddApptCommandParser());
        parsers.put(AddAttdCommand.SUB_COMMAND_WORD, new AddAttdCommandParser());
        this.dispatcher = new SubcommandDispatcherParser<>(parsers, AddCommand.MESSAGE_USAGE);
    }

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public AddCommand parse(String args) throws ParseException {
        return dispatcher.parse(args);
    }
}
