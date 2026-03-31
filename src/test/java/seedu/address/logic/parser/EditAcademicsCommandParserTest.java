package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_SUBJECT_DESC;
import static seedu.address.logic.commands.CommandTestUtil.SUBJECT_DESC_MATH_STRONG;
import static seedu.address.logic.commands.CommandTestUtil.SUBJECT_DESC_SCIENCE_BASIC;
import static seedu.address.logic.commands.EditCommand.MESSAGE_NOT_EDITED;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditAcademicsCommand;
import seedu.address.logic.commands.EditAcademicsCommand.EditAcademicsDescriptor;
import seedu.address.model.academic.Level;
import seedu.address.model.academic.LevelUtil;
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
        // no index
        assertParseFailure(parser, SUBJECT_DESC_MATH_STRONG, MESSAGE_INVALID_SUBJECT_FORMAT);

        // empty input
        assertParseFailure(parser, "", MESSAGE_INVALID_SUBJECT_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        assertParseFailure(parser, "-5" + SUBJECT_DESC_MATH_STRONG, MESSAGE_INVALID_SUBJECT_FORMAT);
        assertParseFailure(parser, "0" + SUBJECT_DESC_MATH_STRONG, MESSAGE_INVALID_SUBJECT_FORMAT);
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_SUBJECT_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_SUBJECT_DESC, Subject.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_singleSubjectWithLevel_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + SUBJECT_DESC_MATH_STRONG;

        EditAcademicsDescriptor descriptor = new EditAcademicsDescriptor();
        descriptor.setSubjects(Set.of(new Subject("Math", Level.STRONG)));

        EditAcademicsCommand expectedCommand =
                new EditAcademicsCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleSubjects_success() {
        Index targetIndex = INDEX_SECOND_PERSON;
        String userInput = targetIndex.getOneBased()
                + SUBJECT_DESC_MATH_STRONG
                + SUBJECT_DESC_SCIENCE_BASIC;

        EditAcademicsDescriptor descriptor = new EditAcademicsDescriptor();
        descriptor.setSubjects(Set.of(
                new Subject("Math", Level.STRONG),
                new Subject("Science", Level.BASIC)
        ));

        EditAcademicsCommand expectedCommand =
                new EditAcademicsCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_subjectWithoutLevel_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + " s/Math";

        EditAcademicsDescriptor descriptor = new EditAcademicsDescriptor();
        descriptor.setSubjects(Set.of(new Subject("Math", null)));

        EditAcademicsCommand expectedCommand =
                new EditAcademicsCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_noteOnly_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + " dsc/Good progress";

        EditAcademicsDescriptor descriptor = new EditAcademicsDescriptor();
        descriptor.setNote("Good progress");

        EditAcademicsCommand expectedCommand =
                new EditAcademicsCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_subjectAndNote_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased()
                + SUBJECT_DESC_MATH_STRONG
                + " dsc/Good";

        EditAcademicsDescriptor descriptor = new EditAcademicsDescriptor();
        descriptor.setSubjects(Set.of(new Subject("Math", Level.STRONG)));
        descriptor.setNote("Good");

        EditAcademicsCommand expectedCommand =
                new EditAcademicsCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_noteBeforeSubjects_success() {
        Index index = INDEX_FIRST_PERSON;
        String input = index.getOneBased() + " dsc/Good s/Math";

        EditAcademicsDescriptor descriptor = new EditAcademicsDescriptor();
        descriptor.setNote("Good");
        descriptor.setSubjects(Set.of(new Subject("Math", null)));

        assertParseSuccess(parser, input,
                new EditAcademicsCommand(index, descriptor));
    }

    @Test
    public void parse_noteInMiddle_success() {
        Index index = INDEX_FIRST_PERSON;
        String input = index.getOneBased() + " s/Math dsc/Good s/English";

        EditAcademicsDescriptor descriptor = new EditAcademicsDescriptor();
        descriptor.setNote("Good");
        descriptor.setSubjects(Set.of(
                new Subject("Math", null),
                new Subject("English", null)
        ));

        assertParseSuccess(parser, input,
                new EditAcademicsCommand(index, descriptor));
    }

    @Test
    public void parse_clearSubjects_success() {
        Index index = INDEX_FIRST_PERSON;
        String input = index.getOneBased() + " s/";

        EditAcademicsDescriptor descriptor = new EditAcademicsDescriptor();
        descriptor.setSubjects(Set.of());

        assertParseSuccess(parser, input,
                new EditAcademicsCommand(index, descriptor));
    }

    @Test
    public void parse_noFields_failure() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = String.valueOf(targetIndex.getOneBased());

        assertParseFailure(parser, userInput, MESSAGE_NOT_EDITED);
    }

    @Test
    public void parse_duplicateSubjects_failure() {
        Index index = INDEX_FIRST_PERSON;
        String input = index.getOneBased() + " s/Math s/Math";

        assertParseFailure(parser, input, "Duplicate subjects are not allowed.");
    }

    @Test
    public void parse_levelWithoutSubject_failure() {
        Index index = INDEX_FIRST_PERSON;
        String input = index.getOneBased() + " l/Strong";

        assertParseFailure(parser, input, "Level must follow a subject.");
    }

    @Test
    public void parse_multipleLevels_failure() {
        Index index = INDEX_FIRST_PERSON;
        String input = index.getOneBased() + " s/Math l/Strong l/Basic";

        assertParseFailure(parser, input, "Each subject can only have one level.");
    }

    @Test
    public void parse_invalidLevel_failure() {
        Index index = INDEX_FIRST_PERSON;
        String input = index.getOneBased() + " s/Math l/Invalid";

        assertParseFailure(parser, input, LevelUtil.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_noteTrimming_success() {
        Index index = INDEX_FIRST_PERSON;
        String input = index.getOneBased() + " dsc/   Good progress   ";

        EditAcademicsDescriptor descriptor = new EditAcademicsDescriptor();
        descriptor.setNote("Good progress");

        assertParseSuccess(parser, input,
                new EditAcademicsCommand(index, descriptor));
    }

    @Test
    public void parse_emptyNote_success() {
        Index index = INDEX_FIRST_PERSON;
        String input = index.getOneBased() + " dsc/";

        EditAcademicsDescriptor descriptor = new EditAcademicsDescriptor();
        descriptor.setNote("");

        assertParseSuccess(parser, input,
                new EditAcademicsCommand(index, descriptor));
    }

    @Test
    public void parse_complexNoteAndSubjects_success() {
        Index index = INDEX_FIRST_PERSON;
        String input = index.getOneBased()
                + " s/Math l/Strong dsc/Good progress, well done!";

        EditAcademicsDescriptor descriptor = new EditAcademicsDescriptor();
        descriptor.setSubjects(Set.of(new Subject("Math", Level.STRONG)));
        descriptor.setNote("Good progress, well done!");

        assertParseSuccess(parser, input,
                new EditAcademicsCommand(index, descriptor));
    }

    @Test
    public void parse_clearSubjectsAndNote_success() {
        Index index = INDEX_FIRST_PERSON;
        String input = index.getOneBased() + " s/ dsc/";

        EditAcademicsDescriptor descriptor = new EditAcademicsDescriptor();
        descriptor.setSubjects(Set.of());
        descriptor.setNote("");

        assertParseSuccess(parser, input,
                new EditAcademicsCommand(index, descriptor));
    }

    @Test
    public void parse_noteAfterSubjects_success() {
        Index index = INDEX_FIRST_PERSON;
        String input = index.getOneBased() + " s/Math dsc/Good";

        EditAcademicsDescriptor descriptor = new EditAcademicsDescriptor();
        descriptor.setSubjects(Set.of(new Subject("Math", null)));
        descriptor.setNote("Good");

        assertParseSuccess(parser, input,
                new EditAcademicsCommand(index, descriptor));
    }
}
