package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_LAST_ATTENDANCE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.LAST_ATTENDANCE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LAST_ATTENDANCE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditAttdCommand;

public class EditAttdCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditAttdCommand.MESSAGE_USAGE);

    private EditAttdCommandParser parser = new EditAttdCommandParser();

    @Test
    public void parse_missingParts_success() throws Exception {
        // EditAttd defaults to current time if no time is provided
        org.junit.jupiter.api.Assertions.assertNotNull(parser.parse("1"));
    }

    @Test
    public void parse_invalidPreamble_failure() {
        assertParseFailure(parser, "-5" + LAST_ATTENDANCE_DESC, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "0" + LAST_ATTENDANCE_DESC, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_LAST_ATTENDANCE_DESC, ParserUtil.MESSAGE_INVALID_DATE_TIME);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + LAST_ATTENDANCE_DESC;
        EditAttdCommand expectedCommand = new EditAttdCommand(targetIndex, LocalDateTime.parse(VALID_LAST_ATTENDANCE));
        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
