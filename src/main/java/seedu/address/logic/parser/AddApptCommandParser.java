package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RECURRENCE;

import java.time.LocalDateTime;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddApptCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.recurrence.Recurrence;

/**
 * Parses input arguments and creates a new {@code AddApptCommand} object.
 */
public class AddApptCommandParser implements Parser<AddApptCommand> {

    @Override
    public AddApptCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String descriptionPrefix = PREFIX_DESCRIPTION.getPrefix();
        int descriptionPrefixPosition = args.indexOf(" " + descriptionPrefix);
        if (descriptionPrefixPosition >= 0) {
            descriptionPrefixPosition++;
        } else if (args.startsWith(descriptionPrefix)) {
            descriptionPrefixPosition = 0;
        }

        if (descriptionPrefixPosition == -1) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    AddApptCommand.MESSAGE_USAGE));
        }

        String argsBeforeDescription = args.substring(0, descriptionPrefixPosition);
        String rawDescription = args.substring(descriptionPrefixPosition + descriptionPrefix.length());

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(argsBeforeDescription,
                PREFIX_DATE, PREFIX_RECURRENCE);

        Index index = ParserUtil.parseIndex(argMultimap.getPreamble(), AddApptCommand.MESSAGE_USAGE);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_DATE, PREFIX_RECURRENCE);

        if (argMultimap.getValue(PREFIX_DATE).isEmpty()) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    AddApptCommand.MESSAGE_USAGE));
        }

        LocalDateTime appointmentStart =
                ParserUtil.parseIsoDateTime(argMultimap.getValue(PREFIX_DATE).get());
        Recurrence recurrence = Recurrence.NONE;
        if (argMultimap.getValue(PREFIX_RECURRENCE).isPresent()) {
            recurrence = ParserUtil.parseRecurrence(argMultimap.getValue(PREFIX_RECURRENCE).get());
        }

        String description = rawDescription.trim();
        if (description.isEmpty()) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    AddApptCommand.MESSAGE_USAGE));
        }

        return new AddApptCommand(index, appointmentStart, recurrence, description);
    }
}
