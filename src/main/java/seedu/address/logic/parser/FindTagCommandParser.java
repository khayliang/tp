package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

import seedu.address.logic.commands.FindTagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new {@code FindTagCommand} object.
 * <p>
 * Expected format: {@code t/TAG [t/MORE_TAGS]...}
 * <p>
 * At least one tag keyword must be provided. Each keyword is treated as a
 * case-insensitive, partial match against a person's tags.
 */
public class FindTagCommandParser implements Parser<FindTagCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindTagCommand
     * and returns a {@code FindTagCommand} object for execution.
     *
     * @param args User input arguments containing one or more {@code t/} prefixes.
     * @return A {@code FindTagCommand} with the parsed tag keywords.
     * @throws ParseException If no valid tag keywords are provided or the input format is invalid.
     */
    @Override
    public FindTagCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(" " + args, PREFIX_TAG);

        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, FindTagCommand.MESSAGE_USAGE));
        }

        if (!argMultimap.getValue(PREFIX_TAG).isPresent()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, FindTagCommand.MESSAGE_USAGE));
        }

        boolean hasEmptyTag = argMultimap.getAllValues(PREFIX_TAG).stream()
                .anyMatch(String::isBlank);

        if (hasEmptyTag) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, FindTagCommand.MESSAGE_USAGE));
        }

        Set<Tag> tags = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        return new FindTagCommand(new TagContainsKeywordsPredicate(tags));
    }
}
