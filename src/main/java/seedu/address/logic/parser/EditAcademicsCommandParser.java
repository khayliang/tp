package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.EditCommand.MESSAGE_NOT_EDITED;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditAcademicsCommand;
import seedu.address.logic.commands.EditAcademicsCommand.EditAcademicsDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.academic.Level;
import seedu.address.model.academic.LevelUtil;
import seedu.address.model.academic.Subject;

/**
 * Parses input arguments and creates a new {@code EditAcademicsCommand}.
 */
public class EditAcademicsCommandParser implements Parser<EditAcademicsCommand> {

    @Override
    public EditAcademicsCommand parse(String args) throws ParseException {
        requireNonNull(args);

        String trimmed = args.trim();
        if (trimmed.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                            EditAcademicsCommand.MESSAGE_USAGE));
        }

        // ================= INDEX =================
        String[] split = trimmed.split("\\s+", 2);
        Index index;

        try {
            index = ParserUtil.parseIndex(split[0]);
        } catch (ParseException e) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                            EditAcademicsCommand.MESSAGE_USAGE), e);
        }

        if (split.length == 1) {
            throw new ParseException(MESSAGE_NOT_EDITED);
        }

        String remainder = split[1];
        EditAcademicsDescriptor descriptor = new EditAcademicsDescriptor();

        // ================= PARSE dsc/ FIRST =================
        String subjectLevelPart = remainder;

        int dscIndex = remainder.indexOf(PREFIX_DESCRIPTION.getPrefix());
        if (dscIndex != -1) {

            int start = dscIndex + PREFIX_DESCRIPTION.getPrefix().length();

            int nextSubject = remainder.indexOf(PREFIX_SUBJECT.getPrefix(), start);
            int nextLevel = remainder.indexOf(PREFIX_LEVEL.getPrefix(), start);

            int end = remainder.length();

            if (nextSubject != -1 && nextSubject < end) {
                end = nextSubject;
            }
            if (nextLevel != -1 && nextLevel < end) {
                end = nextLevel;
            }

            String description = remainder.substring(start, end).trim();
            descriptor.setNote(description); // "" = clear

            // remove dsc segment
            String before = remainder.substring(0, dscIndex).trim();
            String after = (end < remainder.length())
                    ? remainder.substring(end).trim()
                    : "";

            subjectLevelPart = (before + " " + after).trim();
        }

        // ================= PARSE s/ l/ =================
        if (!subjectLevelPart.isEmpty()) {

            List<Subject> subjects = new ArrayList<>();
            Subject current = null;

            int i = 0;
            boolean sawEmptySubject = false;

            while (i < subjectLevelPart.length()) {

                if (subjectLevelPart.startsWith(PREFIX_SUBJECT.getPrefix(), i)) {

                    int start = i + PREFIX_SUBJECT.getPrefix().length();
                    int next = findNextPrefix(subjectLevelPart, start);

                    String name = subjectLevelPart.substring(start, next).trim();

                    // Handle clear case: s/
                    if (name.isEmpty()) {
                        descriptor.setSubjects(new HashSet<>());
                        sawEmptySubject = true;
                        i = next;
                        continue;
                    }

                    if (!Subject.isValidSubjectName(name)) {
                        throw new ParseException(Subject.MESSAGE_CONSTRAINTS);
                    }

                    current = new Subject(name, null);
                    subjects.add(current);

                    i = next;

                } else if (subjectLevelPart.startsWith(PREFIX_LEVEL.getPrefix(), i)) {

                    if (current == null) {
                        throw new ParseException("Level must follow a subject.");
                    }

                    if (current.getLevel().isPresent()) {
                        throw new ParseException("Each subject can only have one level.");
                    }

                    int start = i + PREFIX_LEVEL.getPrefix().length();
                    int next = findNextPrefix(subjectLevelPart, start);

                    String levelStr = subjectLevelPart.substring(start, next).trim();

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
                            String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                                    EditAcademicsCommand.MESSAGE_USAGE));
                }
            }

            // only set subjects if NOT clearing
            if (!sawEmptySubject) {
                Set<String> seen = new HashSet<>();
                for (Subject s : subjects) {
                    if (!seen.add(s.getName())) {
                        throw new ParseException("Duplicate subjects are not allowed.");
                    }
                }
                descriptor.setSubjects(new HashSet<>(subjects));
            }
        }

        // ================= FINAL VALIDATION =================
        if (!descriptor.isSubjectsEdited() && !descriptor.isNoteEdited()) {
            throw new ParseException(MESSAGE_NOT_EDITED);
        }

        return new EditAcademicsCommand(index, descriptor);
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
