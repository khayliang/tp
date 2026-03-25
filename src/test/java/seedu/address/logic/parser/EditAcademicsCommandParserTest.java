package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_SUBJECT_DESC;
import static seedu.address.logic.commands.CommandTestUtil.SUBJECT_DESC_MATH_STRONG;
import static seedu.address.logic.commands.CommandTestUtil.SUBJECT_DESC_SCIENCE_BASIC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditAcademicsCommand;
import seedu.address.model.academic.Academics;
import seedu.address.model.academic.Level;
import seedu.address.model.academic.Subject;

/**
 * Tests for EditAcademicsCommandParser.
 */
public class EditAcademicsCommandParserTest {

    private static final String SUBJECT_EMPTY = " " + PREFIX_SUBJECT;

    private static final String MESSAGE_INVALID_SUBJECT_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditAcademicsCommand.MESSAGE_USAGE);

    private EditAcademicsCommandParser parser = new EditAcademicsCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, SUBJECT_DESC_MATH_STRONG, MESSAGE_INVALID_SUBJECT_FORMAT);

        // no input at all
        assertParseFailure(parser, "", MESSAGE_INVALID_SUBJECT_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + SUBJECT_DESC_MATH_STRONG, MESSAGE_INVALID_SUBJECT_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + SUBJECT_DESC_MATH_STRONG, MESSAGE_INVALID_SUBJECT_FORMAT);

        // invalid arguments
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_SUBJECT_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid subject format
        assertParseFailure(parser, "1" + INVALID_SUBJECT_DESC, Subject.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_singleSubjectWithLevel_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + SUBJECT_DESC_MATH_STRONG;

        Set<Subject> subjects = Set.of(new Subject("Math", Level.STRONG));
        Academics academics = new Academics(subjects);

        EditAcademicsCommand expectedCommand = new EditAcademicsCommand(targetIndex, academics);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleSubjects_success() {
        Index targetIndex = INDEX_SECOND_PERSON;
        String userInput = targetIndex.getOneBased()
                + SUBJECT_DESC_MATH_STRONG
                + SUBJECT_DESC_SCIENCE_BASIC;

        Set<Subject> subjects = Set.of(
                new Subject("Math", Level.STRONG),
                new Subject("Science", Level.BASIC)
        );
        Academics academics = new Academics(subjects);

        EditAcademicsCommand expectedCommand = new EditAcademicsCommand(targetIndex, academics);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_subjectWithoutLevel_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + " s/Math";

        Set<Subject> subjects = Set.of(new Subject("Math", null));
        Academics academics = new Academics(subjects);

        EditAcademicsCommand expectedCommand = new EditAcademicsCommand(targetIndex, academics);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_resetSubjects_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = String.valueOf(targetIndex.getOneBased());

        Set<Subject> subjects = Set.of();
        Academics academics = new Academics(subjects);

        EditAcademicsCommand expectedCommand = new EditAcademicsCommand(targetIndex, academics);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_emptySubject_failure() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + SUBJECT_EMPTY;

        assertParseFailure(parser, userInput, Subject.MESSAGE_CONSTRAINTS);
    }
}
