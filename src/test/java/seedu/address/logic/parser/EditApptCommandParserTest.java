package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.APPOINTMENT_START_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_APPOINTMENT_START_DESC;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_START;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditApptCommand;

public class EditApptCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditApptCommand.MESSAGE_USAGE);

    private EditApptCommandParser parser = new EditApptCommandParser();

    @Test
    public void parse_missingParts_failure() {
        assertParseFailure(parser, "1", seedu.address.logic.commands.EditCommand.MESSAGE_NOT_EDITED);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        assertParseFailure(parser, "-5" + APPOINTMENT_START_DESC, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "0" + APPOINTMENT_START_DESC, MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_APPOINTMENT_START_DESC, ParserUtil.MESSAGE_INVALID_DATE_TIME);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + APPOINTMENT_START_DESC;
        EditApptCommand expectedCommand = new EditApptCommand(targetIndex,
                LocalDateTime.parse(VALID_APPOINTMENT_START));
        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
