package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;

import java.time.Clock;
import java.time.LocalDate;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.AppClock;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditPaymentCommand;
import seedu.address.logic.parser.exceptions.ParseException;
/**
 * Parses input arguments and creates a new {@code EditPaymentCommand} object.
 */
public class EditPaymentCommandParser implements Parser<EditPaymentCommand> {

    private final Clock clock;

    public EditPaymentCommandParser() {
        this(AppClock.getClock());
    }

    EditPaymentCommandParser(Clock clock) {
        requireNonNull(clock);
        this.clock = clock;
    }

    /**
     * Parses the given {@code String} of arguments in the context of the EditPaymentCommand
     * and returns an EditPaymentCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public EditPaymentCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_DATE);

        Index index = ParserUtil.parseIndex(argMultimap.getPreamble(), EditPaymentCommand.MESSAGE_USAGE);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_DATE);

        if (argMultimap.getValue(PREFIX_DATE).isEmpty()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        LocalDate paymentDate =
                ParserUtil.parseIsoDateNotAfterToday(argMultimap.getValue(PREFIX_DATE).get(), clock);

        return new EditPaymentCommand(index, paymentDate);
    }
}
