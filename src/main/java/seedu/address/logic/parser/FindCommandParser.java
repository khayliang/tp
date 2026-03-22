package seedu.address.logic.parser;

import java.util.HashMap;
import java.util.Map;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.FindPersonCommand;
import seedu.address.logic.commands.FindTagCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments for find and dispatches to the relevant find subcommand parser.
 */
public class FindCommandParser implements Parser<FindCommand> {

    private final SubcommandDispatcherParser<FindCommand> dispatcher;

    /**
     * Constructs a FindCommandParser and initializes the subcommand parsers.
     */
    public FindCommandParser() {
        Map<String, Parser<? extends FindCommand>> parsers = new HashMap<>();
        parsers.put(FindPersonCommand.SUB_COMMAND_WORD, new FindPersonCommandParser());
        parsers.put(FindTagCommand.SUB_COMMAND_WORD, new FindTagCommandParser());
        this.dispatcher = new SubcommandDispatcherParser<>(parsers, FindCommand.MESSAGE_USAGE);
    }

    /**
     * Parses the given {@code String} of arguments in the context of find
     * and returns a concrete find subcommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public FindCommand parse(String args) throws ParseException {
        return dispatcher.parse(args);
    }

}
