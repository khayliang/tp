package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AMOUNT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;

import java.time.LocalDate;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditBillingCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code EditBillingCommand} object.
 */
public class EditBillingCommandParser implements Parser<EditBillingCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditBillingCommand
     * and returns an EditBillingCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public EditBillingCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_AMOUNT, PREFIX_DATE);

        Index index = ParserUtil.parseIndex(argMultimap.getPreamble(), EditBillingCommand.MESSAGE_USAGE);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_AMOUNT, PREFIX_DATE);

        Optional<String> amountValue = argMultimap.getValue(PREFIX_AMOUNT);
        Optional<String> dateValue = argMultimap.getValue(PREFIX_DATE);

        if (amountValue.isEmpty() && dateValue.isEmpty()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        Optional<Double> tuitionFee = Optional.empty();
        Optional<LocalDate> paymentDueDate = Optional.empty();
        if (amountValue.isPresent()) {
            tuitionFee = Optional.of(ParserUtil.parseAmount(amountValue.get()));
        }
        if (dateValue.isPresent()) {
            paymentDueDate = Optional.of(ParserUtil.parseIsoDate(dateValue.get()));
        }

        return new EditBillingCommand(index, tuitionFee, paymentDueDate);
    }
}
