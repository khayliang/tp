package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.commands.EditCommand.MESSAGE_NOT_EDITED;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditTagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new {@code EditTagCommand} object.
 * t/        → empty list → empty set → CLEAR
 * t/A t/B   → normal set
 */
public class EditTagCommandParser implements Parser<EditTagCommand> {

    @Override
    public EditTagCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        Index index = ParserUtil.parseIndex(
                argMultimap.getPreamble(),
                EditTagCommand.MESSAGE_USAGE);

        if (!argMultimap.getValue(PREFIX_TAG).isPresent()
                && argMultimap.getAllValues(PREFIX_TAG).isEmpty()) {
            throw new ParseException(MESSAGE_NOT_EDITED);
        }

        java.util.List<String> rawTags = argMultimap.getAllValues(PREFIX_TAG);

        // must have at least one t/
        if (rawTags.isEmpty()) {
            throw new ParseException(MESSAGE_NOT_EDITED);
        }

        java.util.List<String> trimmed = rawTags.stream()
                .map(String::trim)
                .toList();

        boolean allEmpty = trimmed.stream().allMatch(String::isEmpty);
        boolean anyEmpty = trimmed.stream().anyMatch(String::isEmpty);

        Set<Tag> tags;

        // CASE 1: exactly one empty: clear
        // CASE 2: mixture -> INVALID
        // CASE 3: normal tags
        if (allEmpty) {
            if (trimmed.size() > 1) {
                throw new ParseException("Multiple empty tag prefixes are not allowed.");
            }
            tags = Set.of(); // clear
        } else if (anyEmpty) {
            throw new ParseException("Tag prefixes must either all be empty or all be non-empty.");
        } else {
            tags = ParserUtil.parseTags(trimmed);
        }

        return new EditTagCommand(index, tags);
    }
}
