package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import seedu.address.logic.commands.Command;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Shared parser that dispatches to a concrete subcommand parser based on the first token.
 *
 * @param <T> command base type for the subcommand family
 */
public class SubcommandDispatcherParser<T extends Command> implements Parser<T> {

    private final Map<String, Parser<? extends T>> subcommandParsers;
    private final String usageMessage;

    /**
     * Creates a dispatcher with the provided subcommand parser mapping and usage message.
     */
    public SubcommandDispatcherParser(Map<String, Parser<? extends T>> subcommandParsers, String usageMessage) {
        requireNonNull(subcommandParsers);
        requireNonNull(usageMessage);
        this.subcommandParsers = Collections.unmodifiableMap(new HashMap<>(subcommandParsers));
        this.usageMessage = usageMessage;
    }

    @Override
    public T parse(String args) throws ParseException {
        requireNonNull(args);

        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, usageMessage));
        }

        String[] parts = trimmedArgs.split("\\s+", 2);
        String subcommand = parts[0];
        String subcommandBody = parts.length > 1 ? parts[1] : "";

        Parser<? extends T> subcommandParser = subcommandParsers.get(subcommand);
        if (subcommandParser == null) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, usageMessage));
        }

        return subcommandParser.parse(subcommandBody);
    }
}
