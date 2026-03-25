package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_GROUP1;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_JC;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_GROUP1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_JC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditTagCommand;
import seedu.address.model.tag.Tag;

/**
 * Tests for EditTagCommandParser.
 */
public class EditTagCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_TAG;

    private static final String MESSAGE_INVALID_TAG_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditTagCommand.MESSAGE_USAGE);

    private EditTagCommandParser parser = new EditTagCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, TAG_DESC_JC, MESSAGE_INVALID_TAG_FORMAT);

        // no input at all
        assertParseFailure(parser, "", MESSAGE_INVALID_TAG_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + TAG_DESC_JC, MESSAGE_INVALID_TAG_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + TAG_DESC_JC, MESSAGE_INVALID_TAG_FORMAT);

        // invalid arguments
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_TAG_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid tag
        assertParseFailure(parser, "1" + INVALID_TAG_DESC, Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidTagCharacters_failure() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + INVALID_TAG_DESC;

        assertParseFailure(parser, userInput, Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_singleTag_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + TAG_DESC_JC;

        Set<Tag> tags = Set.of(new Tag(VALID_TAG_JC));

        EditTagCommand expectedCommand = new EditTagCommand(targetIndex, tags);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleTags_success() {
        Index targetIndex = INDEX_SECOND_PERSON;
        String userInput = targetIndex.getOneBased() + TAG_DESC_JC + TAG_DESC_GROUP1;

        Set<Tag> tags = Set.of(new Tag(VALID_TAG_JC), new Tag(VALID_TAG_GROUP1));

        EditTagCommand expectedCommand = new EditTagCommand(targetIndex, tags);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_resetTags_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + "";

        Set<Tag> tags = Set.of();

        EditTagCommand expectedCommand = new EditTagCommand(targetIndex, tags);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_emptyTag_failure() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + TAG_EMPTY;

        assertParseFailure(parser, userInput, Tag.MESSAGE_CONSTRAINTS);
    }
}
