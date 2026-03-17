package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PARENT_NAME;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditParentCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.ParentName;

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
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_PARENT_NAME);

        Index index = ParserUtil.parseIndex(argMultimap.getPreamble(), EditParentCommand.MESSAGE_USAGE);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_PARENT_NAME);

        if (!argMultimap.getValue(PREFIX_PARENT_NAME).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditParentCommand.MESSAGE_USAGE));
        }

        ParentName parentName = ParserUtil.parseParentName(argMultimap.getValue(PREFIX_PARENT_NAME).get());

        return new EditParentCommand(index, parentName);
    }
}
