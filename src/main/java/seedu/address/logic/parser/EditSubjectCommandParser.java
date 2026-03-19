package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditSubjectCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.subject.Level;
import seedu.address.model.subject.Subject;

/**
 * Parses input arguments and creates a new {@code EditSubjectCommand} object.
 * STRICT version: enforces level must immediately follow subject.
 */
public class EditSubjectCommandParser implements Parser<EditSubjectCommand> {

    @Override
    public EditSubjectCommand parse(String args) throws ParseException {
        requireNonNull(args);

        String trimmed = args.trim();
        if (trimmed.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditSubjectCommand.MESSAGE_USAGE));
        }

        // Split index and rest
        String[] split = trimmed.split("\\s+", 2);
        Index index;

        try {
            index = ParserUtil.parseIndex(split[0]);
        } catch (ParseException e) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditSubjectCommand.MESSAGE_USAGE), e);
        }

        // No subjects → clear
        if (split.length == 1) {
            return new EditSubjectCommand(index, new HashSet<>());
        }

        String remainder = split[1];

        List<Subject> subjects = new ArrayList<>();

        Subject current = null;

        String[] tokens = remainder.split("\\s+");

        for (String token : tokens) {
            if (token.startsWith(PREFIX_SUBJECT.getPrefix())) {

                String name = token.substring(PREFIX_SUBJECT.getPrefix().length());

                if (!Subject.isValidSubjectName(name)) {
                    throw new ParseException(Subject.MESSAGE_CONSTRAINTS);
                }

                current = new Subject(name, null);
                subjects.add(current);

            } else if (token.startsWith(PREFIX_LEVEL.getPrefix())) {

                if (current == null) {
                    throw new ParseException("Level must follow a subject.");
                }

                if (current.getLevel().isPresent()) {
                    throw new ParseException("Each subject can only have one level.");
                }

                String levelStr = token.substring(PREFIX_LEVEL.getPrefix().length());

                Level level;
                try {
                    level = Level.fromString(levelStr);
                } catch (IllegalArgumentException e) {
                    throw new ParseException(Level.MESSAGE_CONSTRAINTS);
                }

                // replace last subject with updated one
                subjects.remove(subjects.size() - 1);
                current = new Subject(current.getName(), level);
                subjects.add(current);

            } else {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditSubjectCommand.MESSAGE_USAGE));
            }
        }

        // Check duplicates
        Set<String> seen = new HashSet<>();
        for (Subject s : subjects) {
            if (!seen.add(s.getName())) {
                throw new ParseException("Duplicate subjects are not allowed.");
            }
        }

        return new EditSubjectCommand(index, new HashSet<>(subjects));
    }
}
