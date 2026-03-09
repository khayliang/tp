package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.FindPersonCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments for find and dispatches to the relevant find subcommand parser.
 */
public class FindCommandParser implements Parser<FindCommand> {

    private final Map<String, Parser<? extends FindCommand>> subCommandParsers;

    /**
     * Constructs a FindCommandParser and initializes the subcommand parsers.
     */
    public FindCommandParser() {
        Map<String, Parser<? extends FindCommand>> parsers = new HashMap<>();
        parsers.put(FindPersonCommand.SUB_COMMAND_WORD, new FindPersonCommandParser());
        this.subCommandParsers = Collections.unmodifiableMap(parsers);
    }

    /**
     * Parses the given {@code String} of arguments in the context of find
     * and returns a concrete find subcommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        requireNonNull(args);

        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        String[] splitArgs = trimmedArgs.split("\\s+", 2);
        String subcommand = splitArgs[0];
        String subcommandArgs = splitArgs.length > 1 ? splitArgs[1] : "";

        Parser<? extends FindCommand> subCommandParser = subCommandParsers.get(subcommand);
        if (subCommandParser == null) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
        return subCommandParser.parse(subcommandArgs);
    }

}
