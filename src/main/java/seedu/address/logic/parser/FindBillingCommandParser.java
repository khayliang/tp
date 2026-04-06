package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import seedu.address.logic.commands.FindBillingCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.billing.PaymentDueMonthPredicate;

/**
 * Parses input arguments and creates a new {@code FindPaymentCommand} object.
 * Expected format: d/YYYY-MM
 */
public class FindBillingCommandParser implements Parser<FindBillingCommand> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    @Override
    public FindBillingCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(" " + args, PREFIX_DATE);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_DATE);

        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindBillingCommand.MESSAGE_USAGE));
        }

        if (!argMultimap.getValue(PREFIX_DATE).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindBillingCommand.MESSAGE_USAGE));
        }

        String monthText = argMultimap.getValue(PREFIX_DATE).get().trim();
        if (monthText.isBlank()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindBillingCommand.MESSAGE_USAGE));
        }

        YearMonth month;
        try {
            month = YearMonth.parse(monthText, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindBillingCommand.MESSAGE_USAGE));
        }

        return new FindBillingCommand(new PaymentDueMonthPredicate(month));
    }
}
