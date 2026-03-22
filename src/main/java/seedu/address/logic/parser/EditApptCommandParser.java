package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;

import java.time.LocalDateTime;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditApptCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.parser.exceptions.ParseException;
/**
 * Parses input arguments and creates a new {@code EditApptCommand} object.
 */
public class EditApptCommandParser implements Parser<EditApptCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditApptCommand
     * and returns an EditApptCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public EditApptCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_DATE);

        Index index = ParserUtil.parseIndex(argMultimap.getPreamble(), EditApptCommand.MESSAGE_USAGE);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_DATE);

        if (argMultimap.getValue(PREFIX_DATE).isEmpty()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        LocalDateTime appointmentStart =
                ParserUtil.parseIsoDateTime(argMultimap.getValue(PREFIX_DATE).get());

        return new EditApptCommand(index, appointmentStart);
    }
}
