package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindParentCommand;
import seedu.address.model.person.GuardianContainsKeywordsPredicate;

public class FindParentCommandParserTest {

    private final FindParentCommandParser parser = new FindParentCommandParser();

    // ──────────────────────── failure cases ────────────────────────

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindParentCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_noPrefixes_throwsParseException() {
        // Free-form text without any n/, p/, e/ prefix
        assertParseFailure(parser, "Alice Bob",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindParentCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_unexpectedPreamble_throwsParseException() {
        assertParseFailure(parser, "5 n/Melvin",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindParentCommand.MESSAGE_USAGE));
    }

    // ──────────────────────── success cases — single prefix ────────────────────────

    @Test
    public void parse_namePrefix_returnsFindParentCommand() {
        FindParentCommand expected = new FindParentCommand(new GuardianContainsKeywordsPredicate(
                Arrays.asList("Alice", "Bob"), Collections.emptyList(), Collections.emptyList()));
        // Normal input
        assertParseSuccess(parser, "n/Alice Bob", expected);
        assertParseSuccess(parser, " n/Alice Bob", expected);
        // Extra whitespace
        assertParseSuccess(parser, " n/ Alice  Bob ", expected);
    }

    @Test
    public void parse_phonePrefix_returnsFindParentCommand() {
        FindParentCommand expected = new FindParentCommand(new GuardianContainsKeywordsPredicate(
                Collections.emptyList(), List.of("91234567"), Collections.emptyList()));
        assertParseSuccess(parser, " p/91234567", expected);
    }

    @Test
    public void parse_emailPrefix_returnsFindParentCommand() {
        FindParentCommand expected = new FindParentCommand(new GuardianContainsKeywordsPredicate(
                Collections.emptyList(), Collections.emptyList(), List.of("parent@example.com")));
        assertParseSuccess(parser, " e/parent@example.com", expected);
    }

    // ──────────────────────── success cases — multiple prefixes ────────────────────────

    @Test
    public void parse_nameAndPhone_returnsFindParentCommand() {
        FindParentCommand expected = new FindParentCommand(new GuardianContainsKeywordsPredicate(
                List.of("Alice"), List.of("91234567"), Collections.emptyList()));
        assertParseSuccess(parser, " n/Alice p/91234567", expected);
    }

    @Test
    public void parse_nameAndEmail_returnsFindParentCommand() {
        FindParentCommand expected = new FindParentCommand(new GuardianContainsKeywordsPredicate(
                List.of("Alice"), Collections.emptyList(), List.of("alice@example.com")));
        assertParseSuccess(parser, " n/Alice e/alice@example.com", expected);
    }

    @Test
    public void parse_allThreePrefixes_returnsFindParentCommand() {
        FindParentCommand expected = new FindParentCommand(new GuardianContainsKeywordsPredicate(
                List.of("Alice"), List.of("91234567"), List.of("alice@example.com")));
        assertParseSuccess(parser, " n/Alice p/91234567 e/alice@example.com", expected);
    }

    @Test
    public void parse_mixedCaseKeywords_preservedAsIs() {
        // The parser should pass keywords through unchanged; case-insensitivity is handled in the predicate
        FindParentCommand expected = new FindParentCommand(new GuardianContainsKeywordsPredicate(
                Arrays.asList("ALICE", "bob"), Collections.emptyList(), Collections.emptyList()));
        assertParseSuccess(parser, " n/ALICE bob", expected);
    }
}
