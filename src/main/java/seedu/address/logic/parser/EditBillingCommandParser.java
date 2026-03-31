package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AMOUNT;

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
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_AMOUNT);

        Index index = ParserUtil.parseIndex(argMultimap.getPreamble(), EditBillingCommand.MESSAGE_USAGE);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_AMOUNT);

        if (argMultimap.getValue(PREFIX_AMOUNT).isEmpty()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        double tuitionFee = ParserUtil.parseAmount(argMultimap.getValue(PREFIX_AMOUNT).get());

        return new EditBillingCommand(index, tuitionFee);
    }
}
