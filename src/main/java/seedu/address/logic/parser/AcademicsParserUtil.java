package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.academic.Level;
import seedu.address.model.academic.LevelUtil;
import seedu.address.model.academic.Subject;

/**
 * Utility class for parsing subject and level sequences in academics commands.
 */
public class AcademicsParserUtil {

    private static final Pattern UNKNOWN_PREFIX_TOKEN = Pattern.compile("\\b[\\p{Alnum}]+/");

    /**
     * Parses a string containing subject and level prefixes into a list of subjects.
     * Enforces: level must immediately follow subject, each subject has at most one level.
     * Format errors are wrapped with MESSAGE_INVALID_COMMAND_FORMAT using the given commandUsage.
     *
     * @param input The string to parse (e.g., "s/Math l/Basic s/English")
     * @param commandUsage The command usage string for wrapping format errors
     * @return List of parsed subjects
     * @throws ParseException if parsing fails
     */
    public static List<Subject> parseSubjectLevelSequence(String input, String commandUsage)
            throws ParseException {
        Optional<List<Subject>> result = parseSequence(input, commandUsage, false);
        return result.orElse(new ArrayList<>());
    }

    /**
     * Like {@link #parseSubjectLevelSequence} but also supports the clear-subjects semantics.
     * An empty subject name (s/ with no content before the next prefix) triggers a clear.
     *
     * @return Optional.empty() if a clear was triggered, Optional.of(subjects) otherwise.
     */
    public static Optional<List<Subject>> parseSubjectLevelSequenceAllowClear(
            String input, String commandUsage) throws ParseException {
        return parseSequence(input, commandUsage, true);
    }

    private static Optional<List<Subject>> parseSequence(
            String input, String commandUsage, boolean allowClear) throws ParseException {
        List<Subject> subjects = new ArrayList<>();
        Subject current = null;

        int i = 0;
        while (i < input.length()) {
            if (input.startsWith(PREFIX_SUBJECT.getPrefix(), i)) {
                int start = i + PREFIX_SUBJECT.getPrefix().length();
                int next = findNextPrefix(input, start);

                String name = input.substring(start, next).trim();

                if (containsUnknownPrefixToken(name)) {
                    throw new ParseException(
                            String.format(MESSAGE_INVALID_COMMAND_FORMAT, commandUsage));
                }

                if (name.isEmpty()) {
                    if (allowClear) {
                        return Optional.empty();
                    }
                    throw new ParseException(Subject.MESSAGE_CONSTRAINTS);
                }

                if (!Subject.isValidSubjectName(name)) {
                    throw new ParseException(Subject.MESSAGE_CONSTRAINTS);
                }

                current = new Subject(name, null);
                subjects.add(current);

                i = next;
            } else if (input.startsWith(PREFIX_LEVEL.getPrefix(), i)) {
                if (current == null) {
                    throw new ParseException("Level must follow a subject.");
                }

                if (current.getLevel().isPresent()) {
                    throw new ParseException("Each subject can only have one level.");
                }

                int start = i + PREFIX_LEVEL.getPrefix().length();
                int next = findNextPrefix(input, start);

                String levelStr = input.substring(start, next).trim();

                Level level;
                try {
                    level = LevelUtil.levelFromString(levelStr);
                } catch (IllegalArgumentException e) {
                    throw new ParseException(LevelUtil.MESSAGE_CONSTRAINTS);
                }

                subjects.remove(subjects.size() - 1);
                current = new Subject(current.getName(), level);
                subjects.add(current);

                i = next;
            } else {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, commandUsage));
            }
        }

        return Optional.of(subjects);
    }

    static int findNextPrefix(String input, int start) {
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

    private static boolean containsUnknownPrefixToken(String value) {
        if (value.isEmpty()) {
            return false;
        }

        String normalized = value.trim().toLowerCase();
        if (normalized.startsWith(PREFIX_SUBJECT.getPrefix())
                || normalized.startsWith(PREFIX_LEVEL.getPrefix())) {
            return false;
        }

        return UNKNOWN_PREFIX_TOKEN.matcher(normalized).find();
    }
}
