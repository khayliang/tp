package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PARENT_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PARENT_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PARENT_PHONE;

import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditParentCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

/**
 * Parses input arguments and creates a new {@code EditParentCommand} object.
 */
public class EditParentCommandParser implements Parser<EditParentCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditParentCommand
     * and returns an EditParentCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public EditParentCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_PARENT_NAME, PREFIX_PARENT_PHONE,
                PREFIX_PARENT_EMAIL);

        Index index = ParserUtil.parseIndex(argMultimap.getPreamble(), EditParentCommand.MESSAGE_USAGE);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_PARENT_NAME, PREFIX_PARENT_PHONE, PREFIX_PARENT_EMAIL);

        boolean hasName = argMultimap.getValue(PREFIX_PARENT_NAME).isPresent();
        boolean hasPhone = argMultimap.getValue(PREFIX_PARENT_PHONE).isPresent();
        boolean hasEmail = argMultimap.getValue(PREFIX_PARENT_EMAIL).isPresent();

        if (!hasName && !hasPhone && !hasEmail) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditParentCommand.MESSAGE_USAGE));
        }

        Optional<Name> parentName = hasName
                ? Optional.of(ParserUtil.parseParentName(argMultimap.getValue(PREFIX_PARENT_NAME).get()))
                : Optional.empty();

        Optional<Phone> parentPhone = hasPhone
                ? Optional.of(ParserUtil.parseParentPhone(argMultimap.getValue(PREFIX_PARENT_PHONE).get()))
                : Optional.empty();

        Optional<Email> parentEmail = hasEmail
                ? Optional.of(ParserUtil.parseParentEmail(argMultimap.getValue(PREFIX_PARENT_EMAIL).get()))
                : Optional.empty();

        return new EditParentCommand(index, parentName, parentPhone, parentEmail);
    }
}
