package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindTagCommand;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagContainsKeywordsPredicate;

public class FindTagCommandParserTest {

    private FindTagCommandParser parser = new FindTagCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_noPrefix_throwsParseException() {
        assertParseFailure(parser, "friends",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_prefixOnly_throwsParseException() {
        assertParseFailure(parser, "t/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_singleTag_success() {
        FindTagCommand expected =
                new FindTagCommand(new TagContainsKeywordsPredicate(Set.of(new Tag("friends"))));

        assertParseSuccess(parser, "t/friends", expected);
    }

    @Test
    public void parse_multipleTags_success() {
        FindTagCommand expected =
                new FindTagCommand(new TagContainsKeywordsPredicate(
                        Set.of(new Tag("friends"), new Tag("school"))));

        assertParseSuccess(parser, "t/friends t/school", expected);
    }

    @Test
    public void parse_multipleTagsWithWhitespace_success() {
        FindTagCommand expected =
                new FindTagCommand(new TagContainsKeywordsPredicate(
                        Set.of(new Tag("friends"), new Tag("school"))));

        assertParseSuccess(parser, "   t/friends   t/school   ", expected);
        assertParseSuccess(parser, "\n t/friends \t t/school \n", expected);
    }

    @Test
    public void parse_duplicateTags_success() {
        FindTagCommand expected =
                new FindTagCommand(new TagContainsKeywordsPredicate(
                        Set.of(new Tag("friends"))));

        // duplicate should collapse into Set
        assertParseSuccess(parser, "t/friends t/friends", expected);
    }

    @Test
    public void parse_preamblePresent_throwsParseException() {
        assertParseFailure(parser, "random t/friends",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_caseInsensitive_success() {
        FindTagCommand expected =
                new FindTagCommand(new TagContainsKeywordsPredicate(
                        Set.of(new Tag("Friends"))));

        assertParseSuccess(parser, "t/Friends", expected);
    }
}
