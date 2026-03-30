package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindAcadCommand;
import seedu.address.model.academic.Subject;
import seedu.address.model.academic.SubjectContainsKeywordsPredicate;

public class FindAcadCommandParserTest {

    private FindAcadCommandParser parser = new FindAcadCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindAcadCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_noPrefix_throwsParseException() {
        assertParseFailure(parser, "math",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindAcadCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_prefixOnly_throwsParseException() {
        assertParseFailure(parser, "s/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindAcadCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_singleSubject_success() {
        FindAcadCommand expected =
                new FindAcadCommand(new SubjectContainsKeywordsPredicate(
                        Set.of(new Subject("math", null))));

        assertParseSuccess(parser, "s/math", expected);
    }

    @Test
    public void parse_multipleSubjects_success() {
        FindAcadCommand expected =
                new FindAcadCommand(new SubjectContainsKeywordsPredicate(
                        Set.of(
                                new Subject("math", null),
                                new Subject("science", null))));

        assertParseSuccess(parser, "s/math s/science", expected);
    }

    @Test
    public void parse_multipleSubjectsWithWhitespace_success() {
        FindAcadCommand expected =
                new FindAcadCommand(new SubjectContainsKeywordsPredicate(
                        Set.of(
                                new Subject("math", null),
                                new Subject("science", null))));

        assertParseSuccess(parser, "   s/math   s/science   ", expected);
        assertParseSuccess(parser, "\n s/math \t s/science \n", expected);
    }

    @Test
    public void parse_duplicateSubjects_success() {
        FindAcadCommand expected =
                new FindAcadCommand(new SubjectContainsKeywordsPredicate(
                        Set.of(new Subject("math", null))));

        // duplicate collapses into Set
        assertParseSuccess(parser, "s/math s/math", expected);
    }

    @Test
    public void parse_preamblePresent_throwsParseException() {
        assertParseFailure(parser, "random s/math",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindAcadCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_caseInsensitive_success() {
        FindAcadCommand expected =
                new FindAcadCommand(new SubjectContainsKeywordsPredicate(
                        Set.of(new Subject("Math", null))));

        assertParseSuccess(parser, "s/Math", expected);
    }
}
