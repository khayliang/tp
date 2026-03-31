package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;

import java.time.Clock;
import java.time.LocalDate;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddPaymentCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code AddPaymentCommand} object.
 */
public class AddPaymentCommandParser implements Parser<AddPaymentCommand> {

    private final Clock clock;

    public AddPaymentCommandParser() {
        this(Clock.systemDefaultZone());
    }

    AddPaymentCommandParser(Clock clock) {
        requireNonNull(clock);
        this.clock = clock;
    }

    @Override
    public AddPaymentCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_DATE);

        Index index = ParserUtil.parseIndex(argMultimap.getPreamble(), AddPaymentCommand.MESSAGE_USAGE);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_DATE);

        if (argMultimap.getValue(PREFIX_DATE).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPaymentCommand.MESSAGE_USAGE));
        }

        LocalDate paymentDate = ParserUtil.parseIsoDateNotAfterToday(argMultimap.getValue(PREFIX_DATE).get(), clock);

        return new AddPaymentCommand(index, paymentDate);
    }
}
