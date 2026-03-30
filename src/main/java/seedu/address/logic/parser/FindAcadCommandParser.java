package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;

import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.logic.commands.FindAcadCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.academic.Subject;
import seedu.address.model.academic.SubjectContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new {@code FindAcadCommand} object.
 * <p>
 * Expected format: {@code s/SUBJECT [s/MORE_SUBJECTS]...}
 * <p>
 * At least one subject keyword must be provided. Each keyword is treated as a
 * case-insensitive, partial match against a person's subjects.
 */
public class FindAcadCommandParser implements Parser<FindAcadCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindAcadCommand
     * and returns a {@code FindAcadCommand} object for execution.
     *
     * @param args User input arguments containing one or more {@code s/} prefixes.
     * @return A {@code FindAcadCommand} with the parsed subject keywords.
     * @throws ParseException If no valid subject keywords are provided or the input format is invalid.
     */
    @Override
    public FindAcadCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(" " + args, PREFIX_SUBJECT);

        // reject preamble (e.g. "random s/math")
        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, FindAcadCommand.MESSAGE_USAGE));
        }

        // must have at least one s/
        if (!argMultimap.getValue(PREFIX_SUBJECT).isPresent()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, FindAcadCommand.MESSAGE_USAGE));
        }

        // reject empty s/
        boolean hasEmptySubject = argMultimap.getAllValues(PREFIX_SUBJECT).stream()
                .anyMatch(String::isBlank);

        if (hasEmptySubject) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, FindAcadCommand.MESSAGE_USAGE));
        }

        Set<Subject> subjects = argMultimap.getAllValues(PREFIX_SUBJECT).stream()
                .map(String::trim)
                .map(name -> new Subject(name, null))
                .collect(Collectors.toSet());

        return new FindAcadCommand(
                new SubjectContainsKeywordsPredicate(subjects));
    }
}
