package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.EditCommand.MESSAGE_NOT_EDITED;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LEVEL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditAcademicsCommand;
import seedu.address.logic.commands.EditAcademicsCommand.EditAcademicsDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
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
            // Reject multiple dsc/ fields immediately
            int secondDscIndex = remainder.indexOf(PREFIX_DESCRIPTION.getPrefix(), dscIndex + 1);
            if (secondDscIndex != -1) {
                throw new ParseException("Multiple description fields are not allowed.");
            }

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
            Optional<List<Subject>> parseResult = AcademicsParserUtil
                    .parseSubjectLevelSequenceAllowClear(subjectLevelPart,
                            EditAcademicsCommand.MESSAGE_USAGE);

            if (parseResult.isEmpty()) {
                // s/ with empty name → clear subjects
                descriptor.setSubjects(new HashSet<>());
            } else {
                List<Subject> subjects = parseResult.get();
                Set<String> seen = new HashSet<>();
                for (Subject s : subjects) {
                    if (!seen.add(s.getName().toLowerCase())) {
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
}
