package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddAcademicsCommand;
import seedu.address.model.academic.Level;
import seedu.address.model.academic.LevelUtil;
import seedu.address.model.academic.Subject;

/**
 * Contains unit tests for AddAcademicsCommandParser.
 */
public class AddAcademicsCommandParserTest {

    private AddAcademicsCommandParser parser = new AddAcademicsCommandParser();

    @Test
    public void parse_validArgs_success() {
        Index targetIndex = Index.fromOneBased(1);

        // single subject
        Set<Subject> expectedSubjects = new HashSet<>();
        expectedSubjects.add(new Subject("Math", null));

        assertParseSuccess(parser, "1 s/Math",
                new AddAcademicsCommand(targetIndex, expectedSubjects));

        // subject with level
        Set<Subject> expectedWithLevel = new HashSet<>();
        expectedWithLevel.add(new Subject("Math", Level.STRONG));

        assertParseSuccess(parser, "1 s/Math l/Strong",
                new AddAcademicsCommand(targetIndex, expectedWithLevel));

        // multiple subjects
        Set<Subject> expectedMultiple = new HashSet<>();
        expectedMultiple.add(new Subject("Math", null));
        expectedMultiple.add(new Subject("Science", null));

        assertParseSuccess(parser, "1 s/Math s/Science",
                new AddAcademicsCommand(targetIndex, expectedMultiple));

        // multiple with levels
        Set<Subject> expectedMixed = new HashSet<>();
        expectedMixed.add(new Subject("Math", Level.STRONG));
        expectedMixed.add(new Subject("Science", Level.BASIC));

        assertParseSuccess(parser, "1 s/Math l/Strong s/Science l/Basic",
                new AddAcademicsCommand(targetIndex, expectedMixed));
    }

    @Test
    public void parse_missingParts_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AddAcademicsCommand.MESSAGE_USAGE);

        // no index
        assertParseFailure(parser, "s/Math", expectedMessage);

        // no subject
        assertParseFailure(parser, "1", expectedMessage);

        // empty input
        assertParseFailure(parser, "", expectedMessage);
    }

    @Test
    public void parse_invalidIndex_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AddAcademicsCommand.MESSAGE_USAGE);

        // non-numeric index
        assertParseFailure(parser, "abc s/Math", expectedMessage);
    }

    @Test
    public void parse_invalidSubject_failure() {
        // invalid subject (empty)
        assertParseFailure(parser, "1 s/",
                Subject.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_levelWithoutSubject_failure() {
        assertParseFailure(parser, "1 l/Strong",
                "Level must follow a subject.");
    }

    @Test
    public void parse_duplicateLevel_failure() {
        assertParseFailure(parser, "1 s/Math l/Strong l/Basic",
                "Each subject can only have one level.");
    }

    @Test
    public void parse_invalidLevel_failure() {
        assertParseFailure(parser, "1 s/Math l/Invalid",
                                LevelUtil.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_wrongOrder_failure() {
        // level before subject
        assertParseFailure(parser, "1 l/Strong s/Math",
                "Level must follow a subject.");
    }

    @Test
    public void parse_duplicateSubjects_failure() {
        assertParseFailure(parser, "1 s/Math s/Math",
                "Duplicate subjects are not allowed.");
    }

    @Test
    public void parse_duplicateSubjectsDifferentCase_failure() {
        assertParseFailure(parser, "1 s/Math s/mAtH",
                "Duplicate subjects are not allowed.");
    }

    @Test
    public void parse_unknownPrefixLikeToken_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AddAcademicsCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "1 s/Math desc/progress", expectedMessage);
    }
}
