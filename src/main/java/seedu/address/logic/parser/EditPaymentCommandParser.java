package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPOINTMENT_START;

import java.time.LocalDate;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditPaymentCommand;
import seedu.address.logic.parser.exceptions.ParseException;
/**
 * Parses input arguments and creates a new {@code EditApptCommand} object.
 */
public class EditPaymentCommandParser implements Parser<EditPaymentCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditPaymentCommand
     * and returns an EditPaymentCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public EditPaymentCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_APPOINTMENT_START);

        Index index = ParserUtil.parseIndex(argMultimap.getPreamble(), EditPaymentCommand.MESSAGE_USAGE);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_APPOINTMENT_START);

        if (argMultimap.getValue(PREFIX_APPOINTMENT_START).isEmpty()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        LocalDate paymentDate =
                ParserUtil.parseIsoDate(argMultimap.getValue(PREFIX_APPOINTMENT_START).get());

        return new EditPaymentCommand(index, paymentDate);
    }
}
