package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditPersonCommand;
import seedu.address.logic.commands.EditTagCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    private final Map<String, Parser<? extends EditCommand>> subCommandParsers;

    /**
     * Creates an EditCommandParser and registers available sub-command parsers.
     */
    public EditCommandParser() {
        Map<String, Parser<? extends EditCommand>> parsers = new HashMap<>();
        parsers.put(EditPersonCommand.SUB_COMMAND_WORD, new EditPersonCommandParser());
        parsers.put(EditTagCommand.SUB_COMMAND_WORD, new EditTagCommandParser());
        this.subCommandParsers = Collections.unmodifiableMap(parsers);
    }

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);

        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }

        String[] parts = trimmedArgs.split("\\s+", 2);
        String subCommand = parts[0];
        String body = parts.length > 1 ? parts[1] : "";

        Parser<? extends EditCommand> subCommandParser = subCommandParsers.get(subCommand);
        if (subCommandParser == null) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }

        return subCommandParser.parse(body);
    }
}
