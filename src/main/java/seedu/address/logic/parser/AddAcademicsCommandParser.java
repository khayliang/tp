package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddAcademicsCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.academic.Subject;

/**
 * Parses input arguments and creates a new {@code AddAcademicsCommand} object.
 * STRICT version: enforces level must immediately follow subject.
 */
public class AddAcademicsCommandParser implements Parser<AddAcademicsCommand> {

    @Override
    public AddAcademicsCommand parse(String args) throws ParseException {
        requireNonNull(args);

        String trimmed = args.trim();
        if (trimmed.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                            AddAcademicsCommand.MESSAGE_USAGE));
        }

        // Split index and remainder
        String[] split = trimmed.split("\\s+", 2);
        Index index;

        try {
            index = ParserUtil.parseIndex(split[0]);
        } catch (ParseException e) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                            AddAcademicsCommand.MESSAGE_USAGE), e);
        }

        // Add command MUST have subjects
        if (split.length == 1) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                            AddAcademicsCommand.MESSAGE_USAGE));
        }

        String remainder = split[1];

        List<Subject> subjects = AcademicsParserUtil.parseSubjectLevelSequence(
                remainder, AddAcademicsCommand.MESSAGE_USAGE);

        // Must have at least one subject
        if (subjects.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                            AddAcademicsCommand.MESSAGE_USAGE));
        }

        // Check for duplicate subject names
        Set<String> seen = new HashSet<>();
        for (Subject s : subjects) {
            if (!seen.add(s.getName())) {
                throw new ParseException("Duplicate subjects are not allowed.");
            }
        }

        Set<Subject> subjectSet = new HashSet<>(subjects);

        return new AddAcademicsCommand(index, subjectSet);
    }
}
