package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditAcademicsCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.academic.Academics;
import seedu.address.model.academic.Level;
import seedu.address.model.academic.LevelUtil;
import seedu.address.model.academic.Subject;

/**
 * Parses input arguments and creates a new {@code EditAcademicsCommand} object.
 * STRICT version: enforces level must immediately follow subject.
 */
public class EditAcademicsCommandParser implements Parser<EditAcademicsCommand> {

    @Override
    public EditAcademicsCommand parse(String args) throws ParseException {
        requireNonNull(args);

        String trimmed = args.trim();
        if (trimmed.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditAcademicsCommand.MESSAGE_USAGE));
        }

        // Split index and rest
        String[] split = trimmed.split("\\s+", 2);
        Index index;

        try {
            index = ParserUtil.parseIndex(split[0]);
        } catch (ParseException e) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditAcademicsCommand.MESSAGE_USAGE), e);
        }

        // No subjects → clear
        if (split.length == 1) {
            return new EditAcademicsCommand(index, new Academics());
        }

        String remainder = split[1];

        List<Subject> subjects = new ArrayList<>();
        Subject current = null;

        int i = 0;
        while (i < remainder.length()) {

            if (remainder.startsWith(PREFIX_SUBJECT.getPrefix(), i)) {

                int start = i + PREFIX_SUBJECT.getPrefix().length();

                int nextPrefix = findNextPrefix(remainder, start);
                String name = remainder.substring(start, nextPrefix).trim();

                if (!Subject.isValidSubjectName(name)) {
                    throw new ParseException(Subject.MESSAGE_CONSTRAINTS);
                }

                current = new Subject(name, null);
                subjects.add(current);

                i = nextPrefix;

            } else if (remainder.startsWith(PREFIX_LEVEL.getPrefix(), i)) {

                if (current == null) {
                    throw new ParseException("Level must follow a subject.");
                }

                if (current.getLevel().isPresent()) {
                    throw new ParseException("Each subject can only have one level.");
                }

                int start = i + PREFIX_LEVEL.getPrefix().length();

                int nextPrefix = findNextPrefix(remainder, start);
                String levelStr = remainder.substring(start, nextPrefix).trim();

                Level level;
                try {
                    level = LevelUtil.levelFromString(levelStr);
                } catch (IllegalArgumentException e) {
                    throw new ParseException(LevelUtil.MESSAGE_CONSTRAINTS);
                }

                subjects.remove(subjects.size() - 1);
                current = new Subject(current.getName(), level);
                subjects.add(current);

                i = nextPrefix;

            } else {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditAcademicsCommand.MESSAGE_USAGE));
            }
        }

        return new EditAcademicsCommand(index, new Academics(new HashSet<>(subjects)));
    }

    private int findNextPrefix(String input, int start) {
        int nextSubject = input.indexOf(PREFIX_SUBJECT.getPrefix(), start);
        int nextLevel = input.indexOf(PREFIX_LEVEL.getPrefix(), start);

        if (nextSubject == -1 && nextLevel == -1) {
            return input.length();
        }

        if (nextSubject == -1) {
            return nextLevel;
        }

        if (nextLevel == -1) {
            return nextSubject;
        }

        return Math.min(nextSubject, nextLevel);
    }
}
