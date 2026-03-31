package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_GROUP1;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_JC;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_GROUP1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_JC;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddTagCommand;
import seedu.address.model.tag.Tag;

/**
 * Contains unit tests for AddTagCommandParser.
 */
public class AddTagCommandParserTest {

    private AddTagCommandParser parser = new AddTagCommandParser();

    @Test
    public void parse_validArgs_success() {
        Index targetIndex = Index.fromOneBased(1);

        // single tag
        Set<Tag> expectedTags = new HashSet<>();
        expectedTags.add(new Tag(VALID_TAG_JC));

        assertParseSuccess(parser, "1" + TAG_DESC_JC,
                new AddTagCommand(targetIndex, expectedTags));

        // multiple tags
        Set<Tag> expectedMultipleTags = new HashSet<>();
        expectedMultipleTags.add(new Tag(VALID_TAG_JC));
        expectedMultipleTags.add(new Tag(VALID_TAG_GROUP1));

        assertParseSuccess(parser, "1" + TAG_DESC_JC + TAG_DESC_GROUP1,
                new AddTagCommand(targetIndex, expectedMultipleTags));
    }

    @Test
    public void parse_missingParts_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AddTagCommand.MESSAGE_USAGE);

        // no index
        assertParseFailure(parser, TAG_DESC_JC, expectedMessage);

        // no tag
        assertParseFailure(parser, "1", expectedMessage);

        // empty input
        assertParseFailure(parser, "", expectedMessage);
    }

    @Test
    public void parse_invalidIndex_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AddTagCommand.MESSAGE_USAGE);

        // non-numeric index
        assertParseFailure(parser, "abc" + TAG_DESC_JC, expectedMessage);
    }

    @Test
    public void parse_invalidTag_failure() {
        // invalid tag format
        assertParseFailure(parser, "1" + INVALID_TAG_DESC,
                Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_multipleInvalidTags_failure() {
        // first invalid tag should trigger failure
        assertParseFailure(parser, "1" + INVALID_TAG_DESC + TAG_DESC_JC,
                Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_duplicateTags_success() {
        Index targetIndex = Index.fromOneBased(1);

        Set<Tag> expectedTags = new HashSet<>();
        expectedTags.add(new Tag(VALID_TAG_JC));

        // duplicate tags should still parse (Set handles dedup later)
        assertParseSuccess(parser, "1" + TAG_DESC_JC + TAG_DESC_JC,
                new AddTagCommand(targetIndex, expectedTags));
    }
}
