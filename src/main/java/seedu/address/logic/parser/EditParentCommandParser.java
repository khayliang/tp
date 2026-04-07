package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.Optional;
import java.util.function.Consumer;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditParentCommand;
import seedu.address.logic.commands.EditParentCommand.EditParentDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code EditParentCommand} object.
 */
public class EditParentCommandParser implements Parser<EditParentCommand> {

    @FunctionalInterface
    private interface ThrowingParser<T> {
        T parse(String value) throws ParseException;
    }

    /**
     * Parses the given {@code String} of arguments in the context of the EditParentCommand
     * and returns an EditParentCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public EditParentCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE,
                PREFIX_EMAIL);

        Index index = ParserUtil.parseIndex(argMultimap.getPreamble(), EditParentCommand.MESSAGE_USAGE);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL);

        EditParentDescriptor editParentDescriptor = new EditParentDescriptor();

        parseAndSet(argMultimap, PREFIX_NAME, ParserUtil::parseName,
                editParentDescriptor::setParentName);
        parseAndSet(argMultimap, PREFIX_PHONE, ParserUtil::parsePhone,
                editParentDescriptor::setParentPhone);
        parseAndSet(argMultimap, PREFIX_EMAIL, ParserUtil::parseEmail,
                editParentDescriptor::setParentEmail);

        if (!editParentDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditParentCommand(index, editParentDescriptor);
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
