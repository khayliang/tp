package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPOINTMENT_START;

import java.time.LocalDate;

import seedu.address.logic.commands.ViewApptCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code ViewApptCommand} object.
 */
public class ViewApptCommandParser implements Parser<ViewApptCommand> {

    @Override
    public ViewApptCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            return new ViewApptCommand(LocalDate.now());
        }

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(" " + trimmedArgs, PREFIX_APPOINTMENT_START);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_APPOINTMENT_START);

        if (!argMultimap.getPreamble().isEmpty() || argMultimap.getValue(PREFIX_APPOINTMENT_START).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewApptCommand.MESSAGE_USAGE));
        }

        LocalDate targetDate = ParserUtil.parseIsoDate(argMultimap.getValue(PREFIX_APPOINTMENT_START).get());
        return new ViewApptCommand(targetDate);
    }
}
