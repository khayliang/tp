package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;

import java.time.LocalDate;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddPaymentCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code AddPaymentCommand} object.
 */
public class AddPaymentCommandParser implements Parser<AddPaymentCommand> {

    @Override
    public AddPaymentCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_DATE, PREFIX_AMOUNT);

        Index index = ParserUtil.parseIndex(argMultimap.getPreamble(), AddPaymentCommand.MESSAGE_USAGE);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_DATE, PREFIX_AMOUNT);

        if (argMultimap.getValue(PREFIX_DATE).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPaymentCommand.MESSAGE_USAGE));
        }

        LocalDate paymentDate = ParserUtil.parseIsoDate(argMultimap.getValue(PREFIX_DATE).get());

        Optional<Double> tuitionFee;
        try {
            tuitionFee = argMultimap.getValue(PREFIX_AMOUNT).map(Double::parseDouble);
        } catch (NumberFormatException nfe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPaymentCommand.MESSAGE_USAGE));
        }

        return new AddPaymentCommand(index, paymentDate, tuitionFee);
    }
}
