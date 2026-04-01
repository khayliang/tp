package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PARENT_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PARENT_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PARENT_PHONE;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import seedu.address.logic.commands.FindParentCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.GuardianContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindParentCommand object.
 */
public class FindParentCommandParser implements Parser<FindParentCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindParentCommand
     * and returns a FindParentCommand object for execution.
     * At least one of n/, p/, e/ must be provided.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public FindParentCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_PARENT_NAME, PREFIX_PARENT_PHONE, PREFIX_PARENT_EMAIL);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_PARENT_NAME, PREFIX_PARENT_PHONE, PREFIX_PARENT_EMAIL);

        List<String> nameKeywords = parseKeywords(argMultimap, PREFIX_PARENT_NAME);
        List<String> phoneKeywords = parseKeywords(argMultimap, PREFIX_PARENT_PHONE);
        List<String> emailKeywords = parseKeywords(argMultimap, PREFIX_PARENT_EMAIL);

        if (nameKeywords.isEmpty() && phoneKeywords.isEmpty() && emailKeywords.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindParentCommand.MESSAGE_USAGE));
        }

        return new FindParentCommand(
                new GuardianContainsKeywordsPredicate(nameKeywords, phoneKeywords, emailKeywords));
    }

    private List<String> parseKeywords(ArgumentMultimap argMultimap, Prefix prefix) {
        return argMultimap.getValue(prefix)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> Arrays.asList(s.split("\\s+")))
                .orElse(Collections.emptyList());
    }
}
