package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.Optional;
import java.util.function.Consumer;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditPersonCommand;
import seedu.address.logic.commands.EditPersonCommand.EditPersonDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code EditPersonCommand} object.
 */
public class EditPersonCommandParser implements Parser<EditPersonCommand> {

    @FunctionalInterface
    private interface ThrowingParser<T> {
        T parse(String value) throws ParseException;
    }

    /**
     * Parses the given {@code String} of arguments in the context of the EditPersonCommand
     * and returns an EditPersonCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public EditPersonCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS);

        Index index = ParserUtil.parseIndex(argMultimap.getPreamble(), EditPersonCommand.MESSAGE_USAGE);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS);

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();

        parseAndSet(argMultimap, PREFIX_NAME, ParserUtil::parseName, editPersonDescriptor::setName);
        parseAndSet(argMultimap, PREFIX_PHONE, ParserUtil::parsePhone, editPersonDescriptor::setPhone);
        parseAndSet(argMultimap, PREFIX_EMAIL, ParserUtil::parseEmail, editPersonDescriptor::setEmail);
        parseAndSet(argMultimap, PREFIX_ADDRESS, ParserUtil::parseAddress, editPersonDescriptor::setAddress);

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditPersonCommand(index, editPersonDescriptor);
    }

    private static <T> void parseAndSet(ArgumentMultimap argMultimap, Prefix prefix,
                                        ThrowingParser<T> parser, Consumer<T> setter)
            throws ParseException {
        Optional<String> value = argMultimap.getValue(prefix);
        if (value.isPresent()) {
            setter.accept(parser.parse(value.get()));
        }
    }
}
