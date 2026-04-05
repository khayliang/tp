package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;

import seedu.address.commons.util.AppClock;
import seedu.address.logic.commands.FindApptCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code FindApptCommand} object.
 */
public class FindApptCommandParser implements Parser<FindApptCommand> {

    @Override
    public FindApptCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            return new FindApptCommand(AppClock.today());
        }

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(" " + trimmedArgs, PREFIX_DATE);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_DATE);

        if (!argMultimap.getPreamble().isEmpty() || argMultimap.getValue(PREFIX_DATE).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindApptCommand.MESSAGE_USAGE));
        }

        return new FindApptCommand(ParserUtil.parseIsoDate(argMultimap.getValue(PREFIX_DATE).get()));
    }
}
