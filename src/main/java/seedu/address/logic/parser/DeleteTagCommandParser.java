package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteTagCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteTagCommand object
 */
public class DeleteTagCommandParser implements Parser<DeleteTagCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteTagCommand
     * and returns a DeleteTagCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteTagCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        Index personIndex = ParserUtil.parseIndex(argMultimap.getPreamble(),
                DeleteTagCommand.MESSAGE_USAGE);

        List<String> tagIndexStrings = argMultimap.getAllValues(PREFIX_TAG);
        if (tagIndexStrings.isEmpty()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, DeleteTagCommand.MESSAGE_USAGE));
        }

        List<Index> tagIndices = new ArrayList<>();
        for (String tagIndexString : tagIndexStrings) {
            try {
                tagIndices.add(ParserUtil.parseIndex(tagIndexString));
            } catch (ParseException pe) {
                throw new ParseException("Invalid tag index.", pe);
            }
        }

        return new DeleteTagCommand(personIndex, tagIndices);
    }
}
