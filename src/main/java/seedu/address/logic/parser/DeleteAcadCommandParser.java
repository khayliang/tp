package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteAcadCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteAcadCommand object.
 */
public class DeleteAcadCommandParser implements Parser<DeleteAcadCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteAcadCommand
     * and returns a DeleteAcadCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteAcadCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_SUBJECT);

        Index index = ParserUtil.parseIndex(argMultimap.getPreamble(), DeleteAcadCommand.MESSAGE_USAGE);

        List<String> subjectIndexStrings = argMultimap.getAllValues(PREFIX_SUBJECT);
        if (subjectIndexStrings.isEmpty()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, DeleteAcadCommand.MESSAGE_USAGE));
        }

        List<Index> subjectIndices = new ArrayList<>();
        for (String subjectIndexString : subjectIndexStrings) {
            try {
                subjectIndices.add(ParserUtil.parseIndex(subjectIndexString));
            } catch (ParseException pe) {
                throw new ParseException("Invalid subject index.", pe);
            }
        }

        return new DeleteAcadCommand(index, subjectIndices);
    }
}
